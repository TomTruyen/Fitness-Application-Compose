package com.tomtruyen.feature.exercises

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.ExerciseUiModel

@Immutable
data class ExercisesUiState(
    val mode: ExerciseMode = ExerciseMode.VIEW, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: ExerciseFilter = ExerciseFilter(),
    val selectedExercises: List<ExerciseUiModel> = emptyList(),

    val exercises: List<ExerciseUiModel> = emptyList(),
    val categories: List<CategoryUiModel> = emptyList(),
    val equipment: List<EquipmentUiModel> = emptyList(),

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
