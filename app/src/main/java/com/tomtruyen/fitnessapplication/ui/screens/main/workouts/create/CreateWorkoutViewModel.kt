package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class CreateWorkoutViewModel: BaseViewModel<CreateWorkoutNavigationType>() {
    val state = MutableStateFlow(CreateWorkoutUiState())

    // TODO: OnSave to Firebase --> Set the "order" value for each exercise (index in the list)

    fun onEvent(event: CreateWorkoutUiEvent) {
        when(event) {
            is CreateWorkoutUiEvent.OnReorderExerciseClicked -> navigate(CreateWorkoutNavigationType.ReorderExercise)
            is CreateWorkoutUiEvent.OnAddExerciseClicked -> navigate(CreateWorkoutNavigationType.AddExercise)
            is CreateWorkoutUiEvent.OnReorderExercises -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = event.exercises
                    )
                )
            }
        }
    }
}
