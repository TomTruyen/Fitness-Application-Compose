package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.data.entities.Exercise

sealed class ExercisesUiAction {
    data object OnToggleSearch : ExercisesUiAction()
    data object OnFilterClicked : ExercisesUiAction()
    data object OnAddClicked: ExercisesUiAction()
    data class OnSearchQueryChanged(val query: String) : ExercisesUiAction()
    data class OnCategoryFilterChanged(val category: String) : ExercisesUiAction()
    data class OnEquipmentFilterChanged(val equipment: String) : ExercisesUiAction()
    data object OnClearFilterClicked : ExercisesUiAction()
    data class OnRemoveFilterClicked(val filter: String) : ExercisesUiAction()
    data class OnExerciseClicked(val exercise: com.tomtruyen.data.entities.Exercise) : ExercisesUiAction()
    data class OnAddExerciseToWorkoutClicked(val exercise: com.tomtruyen.data.entities.Exercise) : ExercisesUiAction()
}