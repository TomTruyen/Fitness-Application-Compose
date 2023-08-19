package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.data.entities.Exercise

sealed class ExercisesUiEvent {
    data object OnToggleSearch : ExercisesUiEvent()
    data object OnFilterClicked : ExercisesUiEvent()
    data object OnAddClicked: ExercisesUiEvent()
    data class OnSearchQueryChanged(val query: String) : ExercisesUiEvent()
    data class OnCategoryFilterChanged(val category: String) : ExercisesUiEvent()
    data class OnEquipmentFilterChanged(val equipment: String) : ExercisesUiEvent()
    data object OnClearFilterClicked : ExercisesUiEvent()
    data class OnRemoveFilterClicked(val filter: String) : ExercisesUiEvent()
    data class OnExerciseClicked(val exercise: Exercise) : ExercisesUiEvent()
    data class OnAddExerciseToWorkoutClicked(val exercise: Exercise) : ExercisesUiEvent()
}