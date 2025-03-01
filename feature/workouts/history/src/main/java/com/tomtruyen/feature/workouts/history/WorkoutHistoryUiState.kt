package com.tomtruyen.feature.workouts.history

import androidx.compose.runtime.Immutable

@Immutable
data class WorkoutHistoryUiState(
    val loading: Boolean = false,
    val refreshing: Boolean = false
)
