package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse
import com.tomtruyen.fitnessapplication.networking.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
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

        workoutRepository.saveWorkouts(
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
        val currentState = state.value

        when (event) {
            is CreateWorkoutUiEvent.Save -> save(event.workoutName)
            is CreateWorkoutUiEvent.OnSettingsChanged -> {
                event.settings?.let {
                    state.value = currentState.copy(settings = it)
                }
            }
            is CreateWorkoutUiEvent.OnExerciseNotesChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copy(
                        exercises = currentState.workout.exercises.mapIndexed { index, exercise ->
                            if (index == event.index) exercise.copy(notes = event.notes) else exercise
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnDeleteExerciseClicked -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copy(
                        exercises = currentState.workout.exercises.filterIndexed { index, _ -> index != event.index }
                    ),
                    selectedExerciseId = currentState.workout.exercises.getOrNull(
                        if (event.index > 0) event.index - 1 else if (currentState.workout.exercises.size > 1) event.index + 1 else -1
                    )?.id
                )
            }
            is CreateWorkoutUiEvent.OnReorderExerciseClicked -> navigate(CreateWorkoutNavigationType.ReorderExercise)
            is CreateWorkoutUiEvent.OnAddExerciseClicked -> navigate(CreateWorkoutNavigationType.AddExercise)
            is CreateWorkoutUiEvent.OnAddExercise -> {
                val newExercise = WorkoutExerciseResponse(
                    exercise = event.exercise,
                    rest = currentState.settings.rest,
                    restEnabled = currentState.settings.restEnabled,
                ).apply { sets = listOf(WorkoutSet(workoutExerciseId = this@apply.id)) }

                state.value = currentState.copy(
                    workout = currentState.workout.copy(exercises = currentState.workout.exercises + newExercise),
                    selectedExerciseId = event.exercise.id
                )
            }
            is CreateWorkoutUiEvent.OnReorderExercises -> {
                state.value = currentState.copy(workout = currentState.workout.copy(exercises = event.exercises))
            }
            is CreateWorkoutUiEvent.OnRestEnabledChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copy(
                        exercises = currentState.workout.exercises.mapIndexed { index, exercise ->
                            if (index == event.exerciseIndex) exercise.copy(restEnabled = event.enabled) else exercise
                        }
                    )
                )
            }
            is CreateWorkoutUiEvent.OnRestChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copy(
                        exercises = currentState.workout.exercises.mapIndexed { index, exercise ->
                            if (index == event.exerciseIndex) exercise.copy(rest = event.rest) else exercise
                        }
                    )
                )
            }
        }
    }


    fun onWorkoutEvent(event: WorkoutExerciseEvent) {
        val currentState = state.value

        when (event) {
            is WorkoutExerciseEvent.OnRepsChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copyWithRepsChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        reps = event.reps
                    )
                )
            }

            is WorkoutExerciseEvent.OnWeightChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copyWithWeightChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        weight = event.weight
                    )
                )
            }

            is WorkoutExerciseEvent.OnTimeChanged -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copyWithTimeChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        time = event.time
                    )
                )
            }

            is WorkoutExerciseEvent.OnDeleteSetClicked -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copyWithDeleteSet(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex
                    )
                )
            }

            is WorkoutExerciseEvent.OnAddSetClicked -> {
                state.value = currentState.copy(
                    workout = currentState.workout.copyWithAddSet(
                        exerciseIndex = event.exerciseIndex,
                    )
                )
            }
        }
    }
}
