package com.tomtruyen.feature.workouts

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.WorkoutUiModel

@Immutable
data class WorkoutsUiState(
    val workouts: List<WorkoutUiModel> = emptyList(),
    val selectedWorkoutId: String? = null,

    val showSheet: Boolean = false,
    val loading: Boolean = false,
    val refreshing: Boolean = false
)
