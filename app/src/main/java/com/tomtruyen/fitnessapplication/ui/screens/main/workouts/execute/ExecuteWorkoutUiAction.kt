package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

sealed class ExecuteWorkoutUiAction {
    data object NextExercise : ExecuteWorkoutUiAction()
    data class FinishWorkout(val duration: Long) : ExecuteWorkoutUiAction()
}
