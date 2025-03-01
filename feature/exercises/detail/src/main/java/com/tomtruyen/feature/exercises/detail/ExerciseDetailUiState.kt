package com.tomtruyen.feature.exercises.detail

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.ExerciseUiModel

@Immutable
data class ExerciseDetailUiState(
    val exercise: ExerciseUiModel? = null,

    val loading: Boolean = false,
)
