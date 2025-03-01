package com.tomtruyen.feature.exercises.detail

import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import androidx.compose.runtime.Immutable

@Immutable
data class ExerciseDetailUiState(
    val fullExercise: ExerciseWithCategoryAndEquipment? = null,

    val loading: Boolean = false,
) {
    val exercise get() = fullExercise?.exercise
    val category get() = fullExercise?.category
    val equipment get() = fullExercise?.equipment
}
