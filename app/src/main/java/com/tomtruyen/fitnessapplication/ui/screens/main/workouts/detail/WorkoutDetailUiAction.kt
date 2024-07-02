package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

sealed class WorkoutDetailUiAction {
    data object Edit: WorkoutDetailUiAction()
    data object Delete: WorkoutDetailUiAction()
    data object StartWorkout: WorkoutDetailUiAction()
}
