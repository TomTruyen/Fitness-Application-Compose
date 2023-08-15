package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

sealed class CreateExerciseUiEvent {
    data class OnExerciseNameChanged(val name: String) : CreateExerciseUiEvent()
    data class OnCategoryChanged(val category: String) : CreateExerciseUiEvent()
    data class OnEquipmentChanged(val equipment: String) : CreateExerciseUiEvent()
    data class OnTypeChanged(val type: String) : CreateExerciseUiEvent()
    data object OnSaveClicked : CreateExerciseUiEvent()
}
