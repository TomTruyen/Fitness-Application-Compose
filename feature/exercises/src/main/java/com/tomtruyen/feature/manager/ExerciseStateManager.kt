package com.tomtruyen.feature.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.feature.exercises.ExercisesUiAction
import com.tomtruyen.feature.exercises.ExercisesUiState

class ExerciseStateManager(
    private val updateState: ((ExercisesUiState) -> ExercisesUiState) -> Unit,
): StateManager<ExercisesUiAction> {
    private fun toggleSearch() = updateState {
        it.copy(searching = !it.searching)
    }

    private fun updateSearchQuery(query: String) = updateState {
        it.copy(search = query)
    }

    private fun updateCategoryFilter(category: CategoryUiModel) = updateState {
        it.copy(
            filter = it.filter.copy().apply {
                tryAddCategory(category)
            }
        )
    }

    private fun updateEquipmentFilter(equipment: EquipmentUiModel) = updateState {
        it.copy(
            filter = it.filter.copy().apply {
                tryAddEquipment(equipment)
            }
        )
    }

    private fun clearFilter() = updateState {
        it.copy(filter = ExerciseFilter())
    }

    private fun removeFilter(option: FilterOption) = updateState {
        it.copy(
            filter = it.filter.copy(
                categories = it.filter.categories.filter { filter ->
                    filter != option
                },
                equipment = it.filter.equipment.filter { filter ->
                    filter != option
                }
            )
        )
    }

    override fun onAction(action: ExercisesUiAction) {
        when(action) {
            ExercisesUiAction.Filter.ToggleSearch -> toggleSearch()

            is ExercisesUiAction.Filter.OnSearchQueryChanged -> updateSearchQuery(
                query = action.query
            )

            is ExercisesUiAction.Filter.OnCategoryFilterChanged -> updateCategoryFilter(
                category = action.category
            )

            is ExercisesUiAction.Filter.OnEquipmentFilterChanged -> updateEquipmentFilter(
                equipment = action.equipment
            )

            ExercisesUiAction.Filter.OnClearClicked -> clearFilter()

            is ExercisesUiAction.Filter.OnRemoveClicked -> removeFilter(
                option = action.option
            )

            else -> Unit
        }
    }
}