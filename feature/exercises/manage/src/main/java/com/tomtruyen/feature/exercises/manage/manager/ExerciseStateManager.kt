package com.tomtruyen.feature.exercises.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.feature.exercises.manage.ManageExerciseUiAction
import com.tomtruyen.feature.exercises.manage.ManageExerciseUiState

class ExerciseStateManager(
    private val updateState: ((ManageExerciseUiState) -> ManageExerciseUiState) -> Unit,
): StateManager<ManageExerciseUiAction> {
    private fun updateName(name: String) = updateState {
        it.copy(
            exercise = it.exercise.copy(
                name = name
            ),
            nameValidationResult = it.validateName(name)
        )
    }

    private fun updateCategory(category: FilterOption) = updateState {
        it.copy(
            exercise = it.exercise.copy(
                category = category as CategoryUiModel
            )
        )
    }

    private fun updateEquipment(equipment: FilterOption) = updateState {
        it.copy(
            exercise = it.exercise.copy(
                equipment = equipment as EquipmentUiModel
            )
        )
    }

    private fun updateType(type: ExerciseType) = updateState {
        it.copy(
            exercise = it.exercise.copy(
                type = type
            )
        )
    }

    override fun onAction(action: ManageExerciseUiAction) {
        when(action) {
            is ManageExerciseUiAction.OnExerciseNameChanged -> updateName(
                name = action.name
            )

            is ManageExerciseUiAction.OnCategoryChanged -> updateCategory(
                category = action.category
            )

            is ManageExerciseUiAction.OnEquipmentChanged -> updateEquipment(
                equipment = action.equipment
            )

            is ManageExerciseUiAction.OnTypeChanged -> updateType(
                type = action.type
            )

            else -> Unit
        }
    }
}