package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository : BaseRepository() {
    override val cacheKey: String
        get() = Workout.TABLE_NAME

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
}