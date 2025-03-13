package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.data.models.ui.copyWithAddSet
import com.tomtruyen.data.models.ui.copyWithDeleteSet
import com.tomtruyen.data.models.ui.copyWithRepsChanged
import com.tomtruyen.data.models.ui.copyWithSetCompleted
import com.tomtruyen.data.models.ui.copyWithTimeChanged
import com.tomtruyen.data.models.ui.copyWithWeightChanged
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState

class SetStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit,
) : StateManager<ManageWorkoutUiAction.Set> {
    private fun addSet(id: String) = updateState {
        it.copy(
            workout = it.workout.copyWithAddSet(
                id = id,
            )
        )
    }

    private fun deleteSet(id: String, setIndex: Int) = updateState {
        it.copy(
            workout = it.workout.copyWithDeleteSet(
                id = id,
                setIndex = setIndex
            )
        )
    }

    private fun updateReps(id: String, setIndex: Int, reps: String?) = updateState {
        it.copy(
            workout = it.workout.copyWithRepsChanged(
                id = id,
                setIndex = setIndex,
                reps = reps,
            )
        )
    }

    private fun updateWeight(id: String, setIndex: Int, weight: String?) = updateState {
        it.copy(
            workout = it.workout.copyWithWeightChanged(
                id = id,
                setIndex = setIndex,
                weight = weight
            )
        )
    }

    private fun updateTime(id: String, setIndex: Int, time: Int?) = updateState {
        it.copy(
            workout = it.workout.copyWithTimeChanged(
                id = id,
                setIndex = setIndex,
                time = time
            )
        )
    }

    private fun toggleSetCompleted(id: String, setIndex: Int) = updateState {
        it.copy(
            workout = it.workout.copyWithSetCompleted(
                id = id,
                setIndex = setIndex
            )
        )
    }

    override fun onAction(action: ManageWorkoutUiAction.Set) {
        when (action) {
            is ManageWorkoutUiAction.Set.Add -> addSet(
                id = action.exerciseId
            )

            is ManageWorkoutUiAction.Set.Delete -> deleteSet(
                id = action.exerciseId,
                setIndex = action.setIndex
            )

            is ManageWorkoutUiAction.Set.OnRepsChanged -> updateReps(
                id = action.exerciseId,
                setIndex = action.setIndex,
                reps = action.reps
            )

            is ManageWorkoutUiAction.Set.OnWeightChanged -> updateWeight(
                id = action.exerciseId,
                setIndex = action.setIndex,
                weight = action.weight
            )

            is ManageWorkoutUiAction.Set.OnTimeChanged -> updateTime(
                id = action.exerciseId,
                setIndex = action.setIndex,
                time = action.time
            )

            is ManageWorkoutUiAction.Set.OnToggleCompleted -> toggleSetCompleted(
                id = action.exerciseId,
                setIndex = action.setIndex
            )
        }
    }
}