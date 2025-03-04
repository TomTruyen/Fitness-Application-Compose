package com.tomtruyen.feature.exercises

import com.tomtruyen.data.models.ui.ExerciseUiModel

sealed class ExercisesUiEvent {
    data object NavigateToFilter : ExercisesUiEvent()

    data object NavigateToAdd : ExercisesUiEvent()

    data object NavigateBack : ExercisesUiEvent()

    data class NavigateToDetail(val id: String) : ExercisesUiEvent()

    data class NavigateBackToWorkout(val exercises: List<ExerciseUiModel>) : ExercisesUiEvent()
}
