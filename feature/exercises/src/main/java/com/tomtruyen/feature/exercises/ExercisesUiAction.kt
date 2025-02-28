package com.tomtruyen.feature.exercises

import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment

sealed class ExercisesUiAction {
    data object OnToggleSearch : ExercisesUiAction()

    data object OnFilterClicked : ExercisesUiAction()

    data object OnAddClicked: ExercisesUiAction()

    data class OnSearchQueryChanged(val query: String) : ExercisesUiAction()

    data class OnCategoryFilterChanged(val category: Category) : ExercisesUiAction()

    data class OnEquipmentFilterChanged(val equipment: Equipment) : ExercisesUiAction()

    data object OnClearFilterClicked : ExercisesUiAction()

    data class OnRemoveFilterClicked(val filter: FilterOption) : ExercisesUiAction()

    data class OnExerciseClicked(val exercise: ExerciseWithCategoryAndEquipment) : ExercisesUiAction()

    data object OnAddExerciseToWorkoutClicked : ExercisesUiAction()

    data object OnRefresh: ExercisesUiAction()
}