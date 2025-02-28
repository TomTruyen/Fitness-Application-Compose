package com.tomtruyen.feature.exercises.detail

sealed class ExerciseDetailUiEvent {
    data class NavigateToEdit(val id: String) : ExerciseDetailUiEvent()

    data object NavigateBack : ExerciseDetailUiEvent()
}
