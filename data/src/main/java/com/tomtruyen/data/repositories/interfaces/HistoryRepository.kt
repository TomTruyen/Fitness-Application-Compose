package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import kotlinx.coroutines.flow.Flow

abstract class HistoryRepository : SyncRepository<WorkoutHistoryWithExercises>() {
    override val cacheKey: String
        get() = WorkoutHistory.TABLE_NAME

    override val dao = database.workoutHistoryDao()

    abstract fun findHistoryByIdAsync(id: String): Flow<WorkoutHistoryUiModel?>

    abstract fun findHistoriesAsync(page: Int): Flow<List<WorkoutHistoryUiModel>>

    abstract suspend fun getWorkoutHistoryPaginated(
        userId: String,
        page: Int,
        refresh: Boolean
    ): Boolean

    abstract suspend fun saveWorkoutHistory(
        userId: String,
        workout: WorkoutUiModel,
    ): String

    abstract suspend fun deleteWorkoutHistory(id: String)
}