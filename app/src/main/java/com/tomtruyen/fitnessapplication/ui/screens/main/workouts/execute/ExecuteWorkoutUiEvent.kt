package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

sealed class ExecuteWorkoutUiEvent {
    data object NavigateToNextExercise : ExecuteWorkoutUiEvent()
    data object NavigateToFinish: ExecuteWorkoutUiEvent()
}
