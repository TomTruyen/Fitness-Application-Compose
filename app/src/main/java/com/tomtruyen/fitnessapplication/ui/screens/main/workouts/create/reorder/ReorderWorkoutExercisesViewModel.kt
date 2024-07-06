package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

class ReorderWorkoutExercisesViewModel(
    exercises: List<WorkoutExerciseResponse>
): BaseViewModel<ReorderWorkoutExercisesUiState, ReorderWorkoutExercisesUiAction, ReorderWorkoutExercisesUiEvent>(
    initialState = ReorderWorkoutExercisesUiState(exercises = exercises)
) {
    override fun onAction(action: ReorderWorkoutExercisesUiAction) {
        when(action) {
            is ReorderWorkoutExercisesUiAction.OnReorder -> updateState {
                it.copy(
                    exercises = it.exercises.toMutableList().apply {
                        val item = this[action.from]
                        removeAt(action.from)
                        add(action.to, item)
                    }
                )
            }

            is ReorderWorkoutExercisesUiAction.OnApplyReorder -> {
                triggerEvent(ReorderWorkoutExercisesUiEvent.OnApplyReorder(uiState.value.exercises))
            }
        }
    }
}
