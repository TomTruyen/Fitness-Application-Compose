package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository: BaseRepository() {
    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>
    abstract suspend fun findWorkouts(): List<WorkoutWithExercises>
    abstract fun findWorkoutByIdAsync(id: String): Flow<WorkoutWithExercises?>
    abstract fun findWorkoutById(id: String): WorkoutWithExercises?
    abstract fun getWorkouts(
        userId: String,
        callback: FirebaseCallback<List<WorkoutResponse>>
    )
    abstract fun saveWorkouts(
        userId: String,
        workouts: List<WorkoutResponse>,
        callback: FirebaseCallback<List<WorkoutResponse>>
    )

    abstract suspend fun deleteWorkout(userId: String, workoutId: String, callback: FirebaseCallback<List<WorkoutResponse>>)

    abstract suspend fun saveWorkoutResponses(responses: List<WorkoutResponse>)
}