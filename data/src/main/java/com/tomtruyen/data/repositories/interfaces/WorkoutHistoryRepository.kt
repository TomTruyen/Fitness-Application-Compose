package com.tomtruyen.data.repositories.interfaces

import androidx.paging.PagingData
import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutHistoryResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutHistoryRepository: BaseRepository() {
    override val identifier: String
        get() = "workout_histories"

    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long,
    ): List<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>

    abstract fun findLastEntryForWorkout(
        workoutId: String,
    ): Flow<com.tomtruyen.data.entities.WorkoutWithExercises?>

    abstract fun getLastEntryForWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<Unit>
    )

    abstract fun getWorkoutHistoriesPaginated(
        userId: String
    ): Flow<PagingData<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>>

    abstract suspend fun finishWorkout(
        userId: String,
        history: WorkoutHistoryResponse,
        callback: FirebaseCallback<Unit>
    )
}