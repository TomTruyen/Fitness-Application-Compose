package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.models.ui.WorkoutUiModel
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository : SyncRepository<WorkoutWithExercises>() {
    override val cacheKey: String
        get() = Workout.TABLE_NAME

    override val dao = database.workoutDao()

    abstract fun findWorkoutsAsync(): Flow<List<WorkoutUiModel>>

    abstract suspend fun findWorkouts(): List<WorkoutUiModel>

    abstract fun findWorkoutByIdAsync(id: String): Flow<WorkoutUiModel?>

    abstract suspend fun findWorkoutById(id: String): WorkoutUiModel?

    abstract suspend fun getWorkouts(
        userId: String,
        refresh: Boolean,
    )

    abstract suspend fun saveWorkout(
        userId: String,
        workout: WorkoutUiModel,
    )

    abstract suspend fun deleteWorkout(workoutId: String)

    abstract suspend fun saveActiveWorkout(workout: WorkoutUiModel)

    abstract suspend fun deleteActiveWorkout()

    abstract suspend fun reorderWorkouts(workouts: List<WorkoutUiModel>)
}