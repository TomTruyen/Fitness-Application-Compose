package com.tomtruyen.fitnessapplication.repositories.interfaces

import androidx.paging.PagingData
import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import kotlinx.coroutines.flow.Flow

abstract class WorkoutHistoryRepository: BaseRepository() {
    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long,
    ): List<WorkoutHistoryWithWorkout>

    abstract fun findLastEntryForWorkout(
        workoutId: String,
    ): Flow<WorkoutWithExercises?>

    abstract fun getLastEntryForWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<Unit>
    )

    abstract fun getWorkoutHistoriesPaginated(
        userId: String
    ): Flow<PagingData<WorkoutHistoryWithWorkout>>

    abstract fun finishWorkout(
        userId: String,
        histories: List<WorkoutHistoryResponse>,
        callback: FirebaseCallback<List<WorkoutHistoryResponse>>
    )
}