package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository: BaseRepository() {
    override val identifier: String
        get() = "workouts"

    abstract fun findWorkoutsAsync(): Flow<List<com.tomtruyen.data.entities.WorkoutWithExercises>>
    abstract suspend fun findWorkouts(): List<com.tomtruyen.data.entities.WorkoutWithExercises>
    abstract fun findWorkoutByIdAsync(id: String): Flow<com.tomtruyen.data.entities.WorkoutWithExercises?>
    abstract fun findWorkoutById(id: String): com.tomtruyen.data.entities.WorkoutWithExercises?
    abstract suspend fun getWorkouts(
        userId: String,
        callback: FirebaseCallback<List<WorkoutResponse>>
    )
    abstract suspend fun saveWorkout(
        userId: String,
        workout: WorkoutResponse,
        isUpdate: Boolean,
        callback: FirebaseCallback<Unit>
    )

    abstract suspend fun deleteWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<Unit>
    )

    abstract suspend fun saveWorkoutResponses(responses: List<WorkoutResponse>)
}