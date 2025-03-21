package com.tomtruyen.feature.exercises.detail

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.ExerciseUiModel

@Immutable
data class ExerciseDetailUiState(
    val exercise: ExerciseUiModel = ExerciseUiModel(),

    val showSheet: Boolean = false,
    val showDialog: Boolean = false,
    val loading: Boolean = false,
)
