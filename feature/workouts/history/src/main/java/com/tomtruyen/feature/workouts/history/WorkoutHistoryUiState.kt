package com.tomtruyen.feature.workouts.history

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel

@Immutable
data class WorkoutHistoryUiState(
    val histories: List<WorkoutHistoryUiModel> = emptyList(),

    val page: Int = WorkoutHistory.INITIAL_PAGE,

    val selectedHistoryId: String? = null,

    val showSheet: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val loading: Boolean = false,
    val refreshing: Boolean = false
)
