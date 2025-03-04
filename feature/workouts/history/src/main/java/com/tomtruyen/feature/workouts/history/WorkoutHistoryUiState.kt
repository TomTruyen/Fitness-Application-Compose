package com.tomtruyen.feature.workouts.history

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel

@Immutable
data class WorkoutHistoryUiState(
    val histories: List<WorkoutHistoryUiModel> = emptyList(),

    val page: Int = WorkoutHistory.INITIAL_PAGE,

    val loading: Boolean = false,
    val refreshing: Boolean = false
)
