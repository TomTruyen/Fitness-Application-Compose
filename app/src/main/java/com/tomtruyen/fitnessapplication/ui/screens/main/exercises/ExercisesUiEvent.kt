package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

sealed class ExercisesUiEvent {
    data object OnToggleSearch : ExercisesUiEvent()
    data object OnFilterClicked : ExercisesUiEvent()
    data object OnAddClicked: ExercisesUiEvent()

    data class OnSearchQueryChanged(val query: String) : ExercisesUiEvent()
}