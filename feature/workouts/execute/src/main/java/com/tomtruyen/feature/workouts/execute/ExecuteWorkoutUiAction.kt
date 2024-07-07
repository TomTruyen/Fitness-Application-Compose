package com.tomtruyen.feature.workouts.execute

sealed class ExecuteWorkoutUiAction {
    data object NextExercise : ExecuteWorkoutUiAction()

    data class FinishWorkout(val duration: Long) : ExecuteWorkoutUiAction()
}
