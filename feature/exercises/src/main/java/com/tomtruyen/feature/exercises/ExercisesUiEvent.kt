package com.tomtruyen.feature.exercises

import com.tomtruyen.data.entities.Exercise

sealed class ExercisesUiEvent {
    data object NavigateToFilter : ExercisesUiEvent()

    data object NavigateToAdd: ExercisesUiEvent()

    data object NavigateBack: ExercisesUiEvent()

    data class NavigateToDetail(val id: String): ExercisesUiEvent()

    data class NavigateBackToWorkout(val exercises: List<Exercise>): ExercisesUiEvent()
}
