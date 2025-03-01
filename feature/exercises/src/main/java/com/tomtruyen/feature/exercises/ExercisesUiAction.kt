package com.tomtruyen.feature.exercises

import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.ExerciseUiModel

sealed class ExercisesUiAction {
    data object OnToggleSearch : ExercisesUiAction()

    data object OnFilterClicked : ExercisesUiAction()

    data object OnAddClicked : ExercisesUiAction()

    data class OnSearchQueryChanged(val query: String) : ExercisesUiAction()

    data class OnCategoryFilterChanged(val category: CategoryUiModel) : ExercisesUiAction()

    data class OnEquipmentFilterChanged(val equipment: EquipmentUiModel) : ExercisesUiAction()

    data object OnClearFilterClicked : ExercisesUiAction()

    data class OnRemoveFilterClicked(val filter: FilterOption) : ExercisesUiAction()

    data class OnExerciseClicked(val exercise: ExerciseUiModel) : ExercisesUiAction()

    data object OnAddExerciseToWorkoutClicked : ExercisesUiAction()

    data object OnRefresh : ExercisesUiAction()
}