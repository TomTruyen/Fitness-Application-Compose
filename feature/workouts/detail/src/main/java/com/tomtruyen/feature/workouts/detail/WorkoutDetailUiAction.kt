package com.tomtruyen.feature.workouts.detail

sealed class WorkoutDetailUiAction {
    data object Edit : WorkoutDetailUiAction()

    data object Delete : WorkoutDetailUiAction()

    data object StartWorkout : WorkoutDetailUiAction()
}
