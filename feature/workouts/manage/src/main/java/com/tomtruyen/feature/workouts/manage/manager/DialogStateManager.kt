package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState

class DialogStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit
) : StateManager<ManageWorkoutUiAction.Dialog> {
    private fun showDeleteDialog(show: Boolean) = updateState {
        it.copy(showDeleteConfirmation = show)
    }

    private fun showFinishDialog(show: Boolean) = updateState {
        it.copy(showFinishConfirmation = show)
    }

    override fun onAction(action: ManageWorkoutUiAction.Dialog) {
        when(action) {
            ManageWorkoutUiAction.Dialog.Workout.Show -> showDeleteDialog(true)
            ManageWorkoutUiAction.Dialog.Workout.Dismiss -> showDeleteDialog(false)

            ManageWorkoutUiAction.Dialog.Finish.Show -> showFinishDialog(true)
            ManageWorkoutUiAction.Dialog.Finish.Dismiss -> showFinishDialog(false)
        }
    }
}