package com.tomtruyen.feature.workouts.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.workouts.WorkoutsUiAction
import com.tomtruyen.feature.workouts.WorkoutsUiState

class DialogStateManager(
    private val updateState: ((WorkoutsUiState) -> WorkoutsUiState) -> Unit,
) : StateManager<WorkoutsUiAction.Dialog> {
    private fun showDiscardDialog(show: Boolean) = updateState {
        it.copy(showDiscardConfirmation = show)
    }

    private fun showDeleteDialog(show: Boolean) = updateState {
        it.copy(showDeleteConfirmation = show)
    }

    override fun onAction(action: WorkoutsUiAction.Dialog) {
        when (action) {
            WorkoutsUiAction.Dialog.Discard.Show -> showDiscardDialog(true)
            WorkoutsUiAction.Dialog.Discard.Dismiss -> showDiscardDialog(false)

            WorkoutsUiAction.Dialog.Workout.Show -> showDeleteDialog(true)
            WorkoutsUiAction.Dialog.Workout.Dismiss -> showDeleteDialog(false)
        }
    }
}