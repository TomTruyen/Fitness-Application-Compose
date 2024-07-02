package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

sealed class ExerciseDetailUiEvent {
    data class NavigateToEdit(val id: String): ExerciseDetailUiEvent()
    data object NavigateBack: ExerciseDetailUiEvent()
}
