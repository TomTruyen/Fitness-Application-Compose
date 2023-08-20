package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow

class CreateWorkoutViewModel(
    settingsRepository: SettingsRepository
): BaseViewModel<CreateWorkoutNavigationType>() {
    val state = MutableStateFlow(CreateWorkoutUiState())

    val settings = settingsRepository.findSettings()

    // TODO: OnSave to Firebase --> Set the "order" value for each exercise (index in the list)

    fun onEvent(event: CreateWorkoutUiEvent) {
        when(event) {
            is CreateWorkoutUiEvent.OnSettingsChanged -> {
                if(event.settings == null) return
                state.value = state.value.copy(
                    settings = event.settings,
                )
            }
            is CreateWorkoutUiEvent.OnExerciseNotesChanged -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.mapIndexed { index, workoutExerciseResponse ->
                            if(index == event.index) {
                                workoutExerciseResponse.copy(
                                    notes = event.notes
                                )
                            } else {
                                workoutExerciseResponse
                            }
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnDeleteExerciseClicked -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.filterIndexed { index, _ ->
                            index != event.index
                        }
                    )
                )
            }
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
