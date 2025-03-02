package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import io.github.jan.supabase.postgrest.from

class WorkoutHistoryRepositoryImpl: WorkoutHistoryRepository() {
    private val dao = database.workoutHistoryDao()
    private val workoutHistoryExerciseDao = database.workoutHistoryExerciseDao()
    private val workoutHistoryExerciseSetDao = database.workoutHistoryExerciseSetDao()

    // TODO: When getting WorkoutHistory -> Use Pagination + Cache based on dates?

    override suspend fun saveWorkoutHistory(userId: String, workout: WorkoutUiModel) {
        val sets = mutableListOf<WorkoutHistoryExerciseSet>()

        val workoutHistory = workout.toWorkoutHistoryEntity(userId)

        val exercises = workout.exercises.mapIndexed { index, exercise ->
            val workoutHistoryExercise = exercise.toWorkoutHistoryExerciseEntity(workoutHistory.id, index)

            sets.addAll(
                exercise.sets.mapIndexed { setIndex, set ->
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
    }
}