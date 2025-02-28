package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository : BaseRepository() {
    override val identifier: String
        get() = Workout.TABLE_NAME

    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>
    abstract suspend fun findWorkouts(): List<WorkoutWithExercises>
    abstract fun findWorkoutByIdAsync(id: String): Flow<WorkoutWithExercises?>
    abstract suspend fun findWorkoutById(id: String): WorkoutWithExercises?
    abstract suspend fun getWorkouts(
        userId: String,
        refresh: Boolean,
    )

    abstract suspend fun saveWorkout(
        userId: String,
        workout: WorkoutWithExercises,
    )

    abstract suspend fun deleteWorkout(
        userId: String,
        workoutId: String,
    )
}