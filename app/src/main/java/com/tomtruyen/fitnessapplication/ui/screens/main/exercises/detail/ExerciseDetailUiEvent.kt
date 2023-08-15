package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

sealed class ExerciseDetailUiEvent {
    data object Edit : ExerciseDetailUiEvent()
    data object Delete : ExerciseDetailUiEvent()
}
