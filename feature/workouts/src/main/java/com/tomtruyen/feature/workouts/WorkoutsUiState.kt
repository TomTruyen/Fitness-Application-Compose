package com.tomtruyen.feature.workouts

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.WorkoutUiModel

@Immutable
data class WorkoutsUiState(
    val workouts: List<WorkoutUiModel> = emptyList(),

    val loading: Boolean = false,
    val refreshing: Boolean = false
)
