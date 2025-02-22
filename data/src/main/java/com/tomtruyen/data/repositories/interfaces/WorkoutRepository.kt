package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutRepository: BaseRepository() {
    override val identifier: String
        get() = "workouts"

    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>
    abstract suspend fun findWorkouts(): List<WorkoutWithExercises>
    abstract fun findWorkoutByIdAsync(id: String): Flow<WorkoutWithExercises?>
    abstract fun findWorkoutById(id: String): WorkoutWithExercises?
    abstract suspend fun getWorkouts(
        userId: String,
        refresh: Boolean,
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