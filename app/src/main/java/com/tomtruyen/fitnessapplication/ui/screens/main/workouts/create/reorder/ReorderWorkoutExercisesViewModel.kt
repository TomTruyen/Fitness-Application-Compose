package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.networking.models.WorkoutExerciseResponse
import kotlinx.coroutines.flow.MutableStateFlow

class ReorderWorkoutExercisesViewModel(
    exercises: List<WorkoutExerciseResponse>
): BaseViewModel<ReorderWorkoutExercisesNavigationType>() {
    val state = MutableStateFlow(
        ReorderWorkoutExercisesUiState(exercises = exercises)
    )

    fun onEvent(event: ReorderWorkoutExercisesUiEvent) {
        when(event) {
            is ReorderWorkoutExercisesUiEvent.OnReorder -> {
                state.value = state.value.copy(
                    exercises = state.value.exercises.toMutableList().apply {
                        val item = this[event.from]
                        removeAt(event.from)
                        add(event.to, item)
                    }
                )
            }
            is ReorderWorkoutExercisesUiEvent.OnApplyReorder -> {
                navigate(ReorderWorkoutExercisesNavigationType.OnApplyReorder(state.value.exercises))
            }
        }
    }
}
