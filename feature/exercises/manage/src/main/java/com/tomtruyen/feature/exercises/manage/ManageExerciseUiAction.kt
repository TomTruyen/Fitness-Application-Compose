package com.tomtruyen.feature.exercises.manage

import com.tomtruyen.core.common.models.FilterOption

sealed class ManageExerciseUiAction {
    data class OnExerciseNameChanged(val name: String) : ManageExerciseUiAction()

    data class OnCategoryChanged(val category: FilterOption) : ManageExerciseUiAction()

    data class OnEquipmentChanged(val equipment: FilterOption) : ManageExerciseUiAction()

    data class OnTypeChanged(val type: String) : ManageExerciseUiAction()

    data object OnSaveClicked : ManageExerciseUiAction()
}
