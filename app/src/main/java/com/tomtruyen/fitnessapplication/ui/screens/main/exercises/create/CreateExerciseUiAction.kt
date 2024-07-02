package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

sealed class CreateExerciseUiAction {
    data class OnExerciseNameChanged(val name: String) : CreateExerciseUiAction()
    data class OnCategoryChanged(val category: String) : CreateExerciseUiAction()
    data class OnEquipmentChanged(val equipment: String) : CreateExerciseUiAction()
    data class OnTypeChanged(val type: String) : CreateExerciseUiAction()
    data object OnSaveClicked : CreateExerciseUiAction()
}
