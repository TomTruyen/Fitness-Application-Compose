package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

sealed class WorkoutDetailUiEvent {
    data class NavigateToEdit(val id: String): WorkoutDetailUiEvent()
    data object NavigateBack: WorkoutDetailUiEvent()
    data class NavigateToStartWorkout(val id: String): WorkoutDetailUiEvent()
}
