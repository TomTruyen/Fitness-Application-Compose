package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState

class NavResultStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit,
    private val onExerciseAction: (ManageWorkoutUiAction.Exercise) -> Unit
) : StateManager<ManageWorkoutUiAction.NavResult> {
    private fun handleExercisesChanged(mode: ExerciseMode, exercises: List<ExerciseUiModel>) {
        when (mode) {
            ExerciseMode.REPLACE -> {
                exercises.firstOrNull()?.let { exercise ->
                    onExerciseAction(ManageWorkoutUiAction.Exercise.Replace(exercise))
                }
            }

            ExerciseMode.SELECT -> {
                onExerciseAction(ManageWorkoutUiAction.Exercise.Add(exercises))
            }

            else -> Unit
        }
    }

    private fun handleWorkoutExercisesReorder(exercises: List<WorkoutExerciseUiModel>) =
        updateState {
            it.copy(
                workout = it.workout.copy(
                    exercises = exercises
                )
            )
        }

    override fun onAction(action: ManageWorkoutUiAction.NavResult) {
        when (action) {
            is ManageWorkoutUiAction.NavResult.Exercises -> handleExercisesChanged(
                mode = action.mode,
                exercises = action.exercises,
            )

            is ManageWorkoutUiAction.NavResult.ReorderWorkoutExercises -> handleWorkoutExercisesReorder(
                exercises = action.exercises
            )
        }
    }
}