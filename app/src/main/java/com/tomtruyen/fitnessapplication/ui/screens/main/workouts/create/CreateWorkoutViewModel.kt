package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import android.util.Log
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
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
            is CreateWorkoutUiEvent.OnRepsChanged -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.mapIndexed { index, workoutExerciseResponse ->
                            if(index == event.exerciseIndex) {
                                workoutExerciseResponse.copy(
                                    sets = workoutExerciseResponse.sets.mapIndexed { setIndex, workoutSetResponse ->
                                        if(setIndex == event.setIndex) {
                                            workoutSetResponse.copy(
                                                reps = event.reps?.toIntOrNull(),
                                                repsString = event.reps
                                            )
                                        } else {
                                            workoutSetResponse
                                        }
                                    }
                                )
                            } else {
                                workoutExerciseResponse
                            }
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnWeightChanged -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.mapIndexed { index, workoutExerciseResponse ->
                            if(index == event.exerciseIndex) {
                                workoutExerciseResponse.copy(
                                    sets = workoutExerciseResponse.sets.mapIndexed { setIndex, workoutSetResponse ->
                                        if(setIndex == event.setIndex) {
                                            workoutSetResponse.copy(
                                                weight = event.weight?.toDoubleOrNull(),
                                                weightString = event.weight
                                            )
                                        } else {
                                            workoutSetResponse
                                        }
                                    }
                                )
                            } else {
                                workoutExerciseResponse
                            }
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnDeleteSetClicked -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.mapIndexed { index, workoutExerciseResponse ->
                            if(index == event.exerciseIndex) {
                                workoutExerciseResponse.copy(
                                    sets = workoutExerciseResponse.sets.filterIndexed { setIndex, _ ->
                                        setIndex != event.setIndex
                                    }
                                )
                            } else {
                                workoutExerciseResponse
                            }
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnAddSetClicked -> {
                state.value = state.value.copy(
                    workout = state.value.workout.copy(
                        exercises = state.value.workout.exercises.mapIndexed { index, workoutExerciseResponse ->
                            if(index == event.exerciseIndex) {
                                workoutExerciseResponse.copy(
                                    sets = workoutExerciseResponse.sets + listOf(
                                        WorkoutSet(
                                            workoutExerciseId = workoutExerciseResponse.id,
                                            order = workoutExerciseResponse.sets.last().order + 1
                                        )
                                    )
                                )
                            } else {
                                workoutExerciseResponse
                            }
                        }
                    )
                )
            }
        }
    }
}
