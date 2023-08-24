package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

sealed class WorkoutDetailUiEvent {
    data object Edit: WorkoutDetailUiEvent()
    data object Delete: WorkoutDetailUiEvent()
}
