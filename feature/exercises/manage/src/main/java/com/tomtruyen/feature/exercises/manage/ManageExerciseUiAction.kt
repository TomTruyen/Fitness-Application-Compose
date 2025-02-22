package com.tomtruyen.feature.exercises.manage

sealed class ManageExerciseUiAction {
    data class OnExerciseNameChanged(val name: String) : ManageExerciseUiAction()

    data class OnCategoryChanged(val category: String) : ManageExerciseUiAction()

    data class OnEquipmentChanged(val equipment: String) : ManageExerciseUiAction()

    data class OnTypeChanged(val type: String) : ManageExerciseUiAction()

    data object OnSaveClicked : ManageExerciseUiAction()
}
