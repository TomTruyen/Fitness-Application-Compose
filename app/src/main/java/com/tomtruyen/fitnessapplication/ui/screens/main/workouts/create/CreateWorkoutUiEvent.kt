package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

sealed class CreateWorkoutUiEvent {
    data object OnReorderExerciseClicked : CreateWorkoutUiEvent()
}
