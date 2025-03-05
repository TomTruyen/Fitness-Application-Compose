package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState

class SheetStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit
): StateManager<ManageWorkoutUiAction.Sheet> {
    private fun setSelectedExerciseId(id: String?) = updateState {
        it.copy(selectedExerciseId = id)
    }

    private fun setSelectedSetIndex(index: Int?) = updateState {
        it.copy(selectedSetIndex = index)
    }

    private fun showWorkoutSheet(show: Boolean) = updateState {
        it.copy(showWorkoutMoreActions = show)
    }

    private fun showExerciseSheet(show: Boolean) = updateState {
        it.copy(showExerciseMoreActions = show)
    }

    private fun showSetSheet(show: Boolean) = updateState {
        it.copy(showSetMoreActions = show)
    }

    override fun onAction(action: ManageWorkoutUiAction.Sheet) {


        when(action) {
            ManageWorkoutUiAction.Sheet.Workout.Show -> showWorkoutSheet(true)

            ManageWorkoutUiAction.Sheet.Workout.Dismiss -> showWorkoutSheet(false)

            is ManageWorkoutUiAction.Sheet.Exercise.Show -> {
                setSelectedExerciseId(action.id)
                showExerciseSheet(true)
            }

            ManageWorkoutUiAction.Sheet.Exercise.Dismiss -> showExerciseSheet(false)

            is ManageWorkoutUiAction.Sheet.Set.Show -> {
                setSelectedExerciseId(action.exerciseId)
                setSelectedSetIndex(action.setIndex)
                showSetSheet(true)
            }

            ManageWorkoutUiAction.Sheet.Set.Dismiss -> showSetSheet(false)
        }
    }
}