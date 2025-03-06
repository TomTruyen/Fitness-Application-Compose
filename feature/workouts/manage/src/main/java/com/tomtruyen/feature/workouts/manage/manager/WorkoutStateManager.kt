package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState

class WorkoutStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit,
) : StateManager<ManageWorkoutUiAction.Workout> {
    private fun updateWorkoutName(name: String) = updateState {
        it.copy(
            workout = it.workout.copy(
                name = name
            ),
        )
    }

    override fun onAction(action: ManageWorkoutUiAction.Workout) {
        when (action) {
            is ManageWorkoutUiAction.Workout.OnNameChanged -> updateWorkoutName(
                name = action.name
            )

            else -> Unit
        }
    }
}