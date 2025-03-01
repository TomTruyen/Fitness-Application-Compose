package com.tomtruyen.feature.workouts.detail

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.WorkoutUiModel

@Immutable
data class WorkoutDetailUiState(
    val workout: WorkoutUiModel? = null,

    val loading: Boolean = false
)
