package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import com.tomtruyen.data.models.mappers.WorkoutExerciseSetUiModelMapper
import com.tomtruyen.data.models.mappers.WorkoutExerciseUiModelMapper
import com.tomtruyen.data.models.mappers.WorkoutHistoryUiModelMapper
import com.tomtruyen.data.models.mappers.WorkoutUiModelMapper
import com.tomtruyen.data.models.network.WorkoutHistoryNetworkModel
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import com.tomtruyen.data.worker.HistorySyncWorker
import com.tomtruyen.data.worker.SyncWorker
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class HistoryRepositoryImpl : HistoryRepository() {
    private val workoutHistoryExerciseDao = database.workoutHistoryExerciseDao()
    private val workoutHistoryExerciseSetDao = database.workoutHistoryExerciseSetDao()
    private val exerciseDao = database.exerciseDao()
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()
    private val workoutDao = database.workoutDao()
    private val previousSetDao = database.previousSetDao()

    private fun calculatePageStart(page: Int): Int {
        return (page - 1).coerceAtLeast(0) * WorkoutHistory.PAGE_SIZE
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findHistoryByIdAsync(id: String) =
        dao.findHistoryByIdAsync(id).mapLatest { history ->
            history?.let(WorkoutHistoryUiModelMapper::fromEntity)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findHistoriesAsync(page: Int) = dao.findHistoriesAsync(
        offset = calculatePageStart(page),
        limit = WorkoutHistory.PAGE_SIZE
    ).mapLatest { histories ->
        histories.map(WorkoutHistoryUiModelMapper::fromEntity)
    }

    override suspend fun getWorkoutHistoryPaginated(
        userId: String,
        page: Int,
        refresh: Boolean
    ): Boolean {
        val pageCacheKey = "${cacheKey}_${page}"

        return fetch(
            refresh = refresh,
            pageCacheKey = pageCacheKey
        ) {
            val from = calculatePageStart(page)
            val to = from + WorkoutHistory.PAGE_SIZE - 1

            return@fetch supabase.from(WorkoutHistory.TABLE_NAME)
                .select(
                    columns = Columns.raw(
                        """
                            *,
                            ${WorkoutHistoryExercise.TABLE_NAME}(
                                *,
                                ${WorkoutHistoryExerciseSet.TABLE_NAME}(*),
                                ${Exercise.TABLE_NAME}(
                                    *,
                                    ${Category.TABLE_NAME}(*),
                                    ${Equipment.TABLE_NAME}(*)
                                )
                            )
                        """.trimIndent()
                    )
                ) {
                    filter {
                        WorkoutHistory::userId eq userId
                    }

                    // Pagination
                    range(
                        from = from.toLong(),
                        to = to.toLong()
                    )

                    order(
                        column = "created_at",
                        order = Order.DESCENDING
                    )
                }
                .decodeList<WorkoutHistoryNetworkModel>()
                .let { response ->
                    // Using MutableList instead of mapping for each one to reduce amount of loops
                    val categories = mutableSetOf<Category>()
                    val equipment = mutableSetOf<Equipment>()
                    val exercises = mutableSetOf<Exercise>()
                    val workoutHistoryExercises = mutableSetOf<WorkoutHistoryExercise>()
                    val sets = mutableSetOf<WorkoutHistoryExerciseSet>()

                    val histories = response.map { history ->
                        history.exercises.forEach { workoutHistoryExercise ->
                            workoutHistoryExercise.exercise?.category?.let { categories.add(it) }
                            workoutHistoryExercise.exercise?.equipment?.let { equipment.add(it) }
                            workoutHistoryExercise.exercise?.toEntity()?.let { exercises.add(it) }

                            sets.addAll(workoutHistoryExercise.sets)

                            workoutHistoryExercises.add(workoutHistoryExercise.toEntity())
                        }

                        history.toEntity(page)
                    }

                    cacheTransaction(pageCacheKey) {
                        if (refresh || page == 1) {
                            // Clear Table
                            dao.deleteAll()
                            cacheDao.deleteStartsWith(cacheKey)
                        }

                        // Add the Entity
                        dao.saveAll(histories)

                        // Add Referenced Items (Relations)
                        categoryDao.saveAll(categories.toList())
                        equipmentDao.saveAll(equipment.toList())
                        exerciseDao.saveAll(exercises.toList())
                        workoutHistoryExerciseDao.saveAll(workoutHistoryExercises.toList())
                        workoutHistoryExerciseSetDao.saveAll(sets.toList())
                    }

                    // We have reached the end once we can't get a full page when fetching data
                    // This will stop us from Fetching More data in the UI
                    val hasReachedEnd = histories.size < WorkoutHistory.PAGE_SIZE

                    return@let hasReachedEnd
                }
        } ?: true
    }

    override suspend fun saveWorkoutHistory(
        userId: String,
        workout: WorkoutUiModel,
    ): String {
        val workoutHistory = WorkoutUiModelMapper.toWorkoutHistoryEntity(
            model = workout,
            userId = userId
        )

        val newPreviousSets = mutableListOf<PreviousSet>()
        val sets = mutableListOf<WorkoutHistoryExerciseSet>()
        val exercises = workout.exercises.filter { exercise ->
            exercise.sets.count(WorkoutExerciseSetUiModel::completed) > 0
        }.mapIndexed { index, exercise ->
            WorkoutExerciseUiModelMapper.toWorkoutHistoryExerciseEntity(
                model = exercise,
                workoutHistoryId = workoutHistory.id,
                index = index
            ).also { workoutHistoryExercise ->
                val historySets = exercise.sets.filter(WorkoutExerciseSetUiModel::completed)
                    .mapIndexed { setIndex, set ->
                        WorkoutExerciseSetUiModelMapper.toWorkoutHistorySetEntity(
                            model = set,
                            workoutHistoryExerciseId = workoutHistoryExercise.id,
                            index = setIndex
                        )
                    }

                sets.addAll(historySets)

                newPreviousSets.addAll(
                    historySets.mapNotNull { set ->
                        workoutHistoryExercise.exerciseId?.let {
                            PreviousSet.fromWorkoutExerciseSet(
                                exerciseId = it,
                                set = set
                            )
                        }
                    }
                )
            }
        }

        transaction {
            dao.save(workoutHistory)
            workoutHistoryExerciseDao.saveAll(exercises)
            workoutHistoryExerciseSetDao.saveAll(sets)

            previousSetDao.saveAll(newPreviousSets)

            workoutDao.deleteById(Workout.ACTIVE_WORKOUT_ID)
        }

        SyncWorker.schedule<HistorySyncWorker>()

        return workoutHistory.id
    }

    override suspend fun sync(item: WorkoutHistoryWithExercises) {
        val workout = item.workoutHistory.copy(synced = true)

        val exercises = item.exercises.map {
            it.workoutHistoryExercise.copy(
                synced = true
            )
        }

        val sets = item.exercises.flatMap { exercise ->
            exercise.sets.map {
                it.copy(synced = true)
            }
        }

        // This won't really do much unless if we support "editing" in the future, but we might so lets just add it
        // Delete "Dangling" Exercises and Sets
        deleteSupabaseDangling(
            table = WorkoutHistoryExercise.TABLE_NAME,
            key = WorkoutHistoryExercise.KEY_ID,
            entitiesToKeep = exercises,
            foreignKeyConstraint = FilterOperation(
                column = WorkoutHistoryExercise.KEY_WORKOUT_HISTORY_ID,
                operator = FilterOperator.EQ,
                value = workout.id
            )
        )

        deleteSupabaseDangling(
            table = WorkoutHistoryExerciseSet.TABLE_NAME,
            key = WorkoutHistoryExerciseSet.KEY_ID,
            entitiesToKeep = sets,
            foreignKeyConstraint = FilterOperation(
                column = WorkoutHistoryExerciseSet.KEY_WORKOUT_HISTORY_EXERCISE_ID,
                operator = FilterOperator.IN,
                value = "(${sets.joinToString(",") { it.workoutHistoryExerciseId }})"
            )
        )

        // Insert new values
        supabase.from(WorkoutHistory.TABLE_NAME).upsert(workout)
        supabase.from(WorkoutHistoryExercise.TABLE_NAME).upsert(exercises)
        supabase.from(WorkoutHistoryExerciseSet.TABLE_NAME).upsert(sets)

        // Update the local items to be "synced"
        transaction {
            dao.save(workout)
            workoutHistoryExerciseDao.saveAll(exercises)
            workoutHistoryExerciseSetDao.saveAll(sets)
        }
    }

    override suspend fun deleteWorkoutHistory(id: String) {
        supabase.from(WorkoutHistory.TABLE_NAME)
            .delete {
                filter {
                    WorkoutHistory::id eq id
                }
            }

        transaction {
            dao.deleteById(id)
        }
    }
}