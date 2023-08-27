package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

sealed class ExecuteWorkoutUiEvent {
    data object NextExercise : ExecuteWorkoutUiEvent()
    data class FinishWorkout(val duration: Long) : ExecuteWorkoutUiEvent()
}
