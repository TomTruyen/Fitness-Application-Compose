package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.models.network.WorkoutHistoryNetworkModel
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class WorkoutHistoryRepositoryImpl: WorkoutHistoryRepository() {
    private val dao = database.workoutHistoryDao()
    private val workoutHistoryExerciseDao = database.workoutHistoryExerciseDao()
    private val workoutHistoryExerciseSetDao = database.workoutHistoryExerciseSetDao()
    private val exerciseDao = database.exerciseDao()
    private val categoryDao = database.categoryDao()
    private val equipmentDao = database.equipmentDao()

    // Page should be > 0
    override suspend fun getWorkoutHistoryPaginated(userId: String, page: Int, refresh: Boolean) {
        val pageCacheKey = "${cacheKey}_${page}"
        val pageSize = 10

        fetch(
            refresh = refresh,
            pageCacheKey = pageCacheKey
        ) {
            val from = (page - 1) * pageSize
            val to = from + pageSize - 1

            supabase.from(WorkoutHistory.TABLE_NAME)
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
                    // TODO: Filter out duplicates before saving? Do the same for WorkoutRepository

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
                        // Clear Table
                        dao.deleteAll()

                        // Add the Entity
                        dao.saveAll(histories)

                        // Add Referenced Items (Relations)
                        categoryDao.saveAll(categories)
                        equipmentDao.saveAll(equipment)
                        exerciseDao.saveAll(exercises)
                        workoutHistoryExerciseDao.saveAll(workoutHistoryExercises)
                        workoutHistoryExerciseSetDao.saveAll(sets)
                    }
                }


        }
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
            val workoutHistoryExercise = exercise.toWorkoutHistoryExerciseEntity(workoutHistory.id, index)

            sets.addAll(
                exercise.sets.filter(WorkoutExerciseSetUiModel::completed).mapIndexed { setIndex, set ->
                    set.toWorkoutHistorySetEntity(workoutHistoryExercise.id, setIndex)
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
        }

        return workoutHistory.id
    }
}