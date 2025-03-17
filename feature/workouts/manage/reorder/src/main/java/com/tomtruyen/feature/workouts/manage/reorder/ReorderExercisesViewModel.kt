package com.tomtruyen.feature.workouts.manage.reorder

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel

class ReorderExercisesViewModel(
    exercises: List<WorkoutExerciseUiModel>
) : BaseViewModel<ReorderExercisesUiState, ReorderExercisesUiAction, ReorderExercisesUiEvent>(
    initialState = ReorderExercisesUiState(
        exercises = exercises
    )
) {
    private fun reorder(from: Int, to: Int) = updateState {
        it.copy(
            exercises = it.exercises.toMutableList().apply {
                add(to, removeAt(from))
            }
        )
    }

    override fun onAction(action: ReorderExercisesUiAction) {
        when (action) {
            is ReorderExercisesUiAction.Reorder -> reorder(
                from = action.from,
                to = action.to
            )

            ReorderExercisesUiAction.Submit -> triggerEvent(
                ReorderExercisesUiEvent.Navigate.Submit(uiState.value.exercises)
            )
        }
    }
}
