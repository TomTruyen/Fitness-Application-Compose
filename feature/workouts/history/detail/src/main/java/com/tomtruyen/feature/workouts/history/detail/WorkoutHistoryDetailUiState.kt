package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel

data class WorkoutHistoryDetailUiState(
    val history: WorkoutHistoryUiModel = WorkoutHistoryUiModel(),

    val loading: Boolean = false,
)