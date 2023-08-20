package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import android.util.Log
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create.CreateExerciseNavigationType
import kotlinx.coroutines.flow.MutableStateFlow

class CreateWorkoutViewModel(
    val id: String?,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    settingsRepository: SettingsRepository
): BaseViewModel<CreateWorkoutNavigationType>() {
    private val isEditing = id != null

    val state = MutableStateFlow(
        CreateWorkoutUiState(isEditing = isEditing)
    )

    val settings = settingsRepository.findSettings()

    init {
        findWorkout()
    }

    private fun findWorkout() = launchLoading {
        if(!isEditing || id == null) return@launchLoading

        workoutRepository.findWorkoutById(id)?.let {
            state.value = state.value.copy(
                initialWorkout = it.toWorkoutResponse(),
                workout = it.toWorkoutResponse()
            )
        }
    }

    private fun save(workoutName: String) = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        val workouts = workoutRepository.findWorkouts().map {
            it.toWorkoutResponse()
        }.toMutableList()

        val workout = state.value.workout.apply {
            exercises.forEachIndexed { index, workoutExerciseResponse ->
                workoutExerciseResponse.order = index
            }
            name = workoutName
            unit = state.value.settings.unit
        }

        if(isEditing) {
            workouts.removeIf { it.id == id }
        }

        workouts.add(workout)

        workoutRepository.saveWorkout(
            userId = userId,
            workouts = workouts,
            callback = object: FirebaseCallback<List<WorkoutResponse>> {
                override fun onSuccess(value: List<WorkoutResponse>) {
                    navigate(CreateWorkoutNavigationType.Back)
                }

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    fun onEvent(event: CreateWorkoutUiEvent) {
        when(event) {
            is CreateWorkoutUiEvent.Save -> save(event.workoutName)
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
                    ),
                    selectedExerciseId = if(event.index > 0) {
                        state.value.workout.exercises.getOrNull(event.index - 1)?.id
                    } else if(state.value.workout.exercises.size > 1) {
                        state.value.workout.exercises.getOrNull(event.index + 1)?.id
                    } else {
                        null
                    }
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
