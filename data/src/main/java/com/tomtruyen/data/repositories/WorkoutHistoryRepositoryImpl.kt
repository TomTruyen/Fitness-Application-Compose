package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.models.network.WorkoutHistoryNetworkModel
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class WorkoutHistoryRepositoryImpl : WorkoutHistoryRepository() {
    private val dao = database.workoutHistoryDao()
    private val workoutHistoryExerciseDao = database.workoutHistoryExerciseDao()
    private val workoutHistoryExerciseSetDao = database.workoutHistoryExerciseSetDao()
    private val exerciseDao = database.exerciseDao()
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()
    private val workoutDao = database.workoutDao()

    private fun calculatePageStart(page: Int): Int {
        return (page - 1).coerceAtLeast(0) * WorkoutHistory.PAGE_SIZE
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findHistoriesAsync(page: Int) = dao.findHistoriesAsync(
        offset = calculatePageStart(page),
        limit = WorkoutHistory.PAGE_SIZE
    ).mapLatest { histories ->
        histories.map(WorkoutHistoryUiModel::fromEntity)
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
        duration: Long
    ): String {
        val sets = mutableListOf<WorkoutHistoryExerciseSet>()

        val workoutHistory = workout.toWorkoutHistoryEntity(userId, duration)

        val exercises = workout.exercises.filter { exercise ->
            exercise.sets.count(WorkoutExerciseSetUiModel::completed) > 0
        }.mapIndexed { index, exercise ->
            val workoutHistoryExercise =
                exercise.toWorkoutHistoryExerciseEntity(workoutHistory.id, index)

            sets.addAll(
                exercise.sets.filter(WorkoutExerciseSetUiModel::completed)
                    .mapIndexed { setIndex, set ->
                        set.toWorkoutHistorySetEntity(
                            workoutHistoryExercise.id,
                            exercise.exerciseId,
                            setIndex
                        )
                    }
            )

            workoutHistoryExercise
        }

        supabase.from(WorkoutHistory.TABLE_NAME).upsert(workoutHistory)
        supabase.from(WorkoutHistoryExercise.TABLE_NAME).upsert(exercises)
        supabase.from(WorkoutHistoryExerciseSet.TABLE_NAME).upsert(sets)

        transaction {
            dao.save(workoutHistory)
            workoutHistoryExerciseDao.saveAll(exercises)
            workoutHistoryExerciseSetDao.saveAll(sets)

            workoutDao.deleteById(Workout.ACTIVE_WORKOUT_ID)
        }

        return workoutHistory.id
    }
}