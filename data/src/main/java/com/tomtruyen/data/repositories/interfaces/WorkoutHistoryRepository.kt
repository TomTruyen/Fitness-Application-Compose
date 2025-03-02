package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.BaseRepository

abstract class WorkoutHistoryRepository : BaseRepository() {
    override val cacheKey: String
        get() = WorkoutHistory.TABLE_NAME

    abstract suspend fun saveWorkoutHistory(
        userId: String,
        workout: WorkoutUiModel,
        duration: Long,
    ): String
}