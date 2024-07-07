package com.tomtruyen.feature.workouts.execute

sealed class ExecuteWorkoutUiEvent {
    data object NavigateToNextExercise : ExecuteWorkoutUiEvent()

    data object NavigateToFinish: ExecuteWorkoutUiEvent()
}
