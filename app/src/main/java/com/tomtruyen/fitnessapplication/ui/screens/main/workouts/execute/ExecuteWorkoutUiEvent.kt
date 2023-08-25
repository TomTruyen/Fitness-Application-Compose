package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

sealed class ExecuteWorkoutUiEvent {
    data object NextExercise : ExecuteWorkoutUiEvent()
    data object FinishWorkout : ExecuteWorkoutUiEvent()
}
