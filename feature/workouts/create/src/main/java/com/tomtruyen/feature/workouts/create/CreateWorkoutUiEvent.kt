package com.tomtruyen.feature.workouts.create

sealed class CreateWorkoutUiEvent {
    data object NavigateToReorderExercise : CreateWorkoutUiEvent()

    data object NavigateToAddExercise: CreateWorkoutUiEvent()

    data object NavigateBack: CreateWorkoutUiEvent()
}
