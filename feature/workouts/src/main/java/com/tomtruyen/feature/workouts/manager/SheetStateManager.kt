package com.tomtruyen.feature.workouts.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.workouts.WorkoutsUiAction
import com.tomtruyen.feature.workouts.WorkoutsUiState

class SheetStateManager(
    private val updateState: ((WorkoutsUiState) -> WorkoutsUiState) -> Unit,
): StateManager<WorkoutsUiAction> {
    private fun setSelectedWorkoutId(id: String?) = updateState {
        it.copy(selectedWorkoutId = id)
    }

    private fun showSheet(show: Boolean) = updateState {
        it.copy(showSheet = show)
    }

    override fun onAction(action: WorkoutsUiAction) {
        when(action) {
            is WorkoutsUiAction.Sheet.Show -> {
                setSelectedWorkoutId(action.id)
                showSheet(true)
            }

            WorkoutsUiAction.Sheet.Dismiss -> showSheet(false)

            else -> Unit
        }
    }
}