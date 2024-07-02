package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.data.entities.Exercise

sealed class ExercisesUiEvent {
    data object NavigateToFilter : ExercisesUiEvent()
    data object NavigateToAdd: ExercisesUiEvent()
    data object NavigateBack: ExercisesUiEvent()
    data class NavigateToDetail(val id: String): ExercisesUiEvent()
    data class NavigateBackToWorkout(val exercise: Exercise): ExercisesUiEvent()
}
