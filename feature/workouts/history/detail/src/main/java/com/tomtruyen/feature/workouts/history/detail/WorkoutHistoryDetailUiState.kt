package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel

data class WorkoutHistoryDetailUiState(
    val history: WorkoutHistoryUiModel = WorkoutHistoryUiModel(),

    val showSheet: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val loading: Boolean = false,
)