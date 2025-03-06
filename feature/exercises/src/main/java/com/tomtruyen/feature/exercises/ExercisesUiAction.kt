package com.tomtruyen.feature.exercises

import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.ExerciseUiModel

sealed class ExercisesUiAction {
    sealed class Workout: ExercisesUiAction() {
        data object AddExercise: Workout()
    }

    sealed class Filter: ExercisesUiAction() {
        data object ToggleSearch: Filter()

        data object OnClearClicked: Filter()

        data class OnRemoveClicked(val option: FilterOption): Filter()

        data class OnSearchQueryChanged(val query: String): Filter()

        data class OnCategoryFilterChanged(val category: CategoryUiModel): Filter()

        data class OnEquipmentFilterChanged(val equipment: EquipmentUiModel): Filter()
    }

    data object OnFilterClicked: ExercisesUiAction()

    data object OnAddClicked: ExercisesUiAction()

    data class OnDetailClicked(val exercise: ExerciseUiModel): ExercisesUiAction()

    data object OnRefresh : ExercisesUiAction()
}