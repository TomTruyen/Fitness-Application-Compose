package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

sealed class CreateExerciseUiEvent {
    data object NavigateBack : CreateExerciseUiEvent()
}
