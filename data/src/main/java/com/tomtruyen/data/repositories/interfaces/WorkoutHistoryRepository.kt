package com.tomtruyen.data.repositories.interfaces

import androidx.paging.PagingData
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutHistoryResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutHistoryRepository: BaseRepository() {
    override val identifier: String
        get() = WorkoutHistory.TABLE_NAME

    abstract suspend fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long,
    ): List<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>

    abstract fun getWorkoutHistoriesPaginated(
        userId: String
    ): Flow<PagingData<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>>

    abstract suspend fun finishWorkout(
        userId: String,
        history: WorkoutHistoryResponse,
        callback: FirebaseCallback<Unit>
    )
}