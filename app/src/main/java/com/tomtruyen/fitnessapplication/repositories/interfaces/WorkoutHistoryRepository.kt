package com.tomtruyen.fitnessapplication.repositories.interfaces

import androidx.paging.PagingData
import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutHistoryRepository: BaseRepository() {
    abstract fun findWorkoutHistoriesAsync(): Flow<List<WorkoutHistoryWithWorkout>>

    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long,
    ): List<WorkoutHistoryWithWorkout>

    abstract fun getWorkoutHistoriesPaginated(
        userId: String,
    ): Flow<PagingData<WorkoutHistoriesResponse>>

    abstract fun finishWorkout(
        userId: String,
        histories: List<WorkoutHistoryResponse>,
        callback: FirebaseCallback<List<WorkoutHistoryResponse>>
    )
}