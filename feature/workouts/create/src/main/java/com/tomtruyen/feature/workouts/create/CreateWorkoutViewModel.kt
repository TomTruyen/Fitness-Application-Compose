package com.tomtruyen.feature.workouts.create

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.entities.WorkoutSet
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.log

class CreateWorkoutViewModel(
    private val id: String?,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val settingsRepository: SettingsRepository
): BaseViewModel<CreateWorkoutUiState, CreateWorkoutUiAction, CreateWorkoutUiEvent>(
    initialState = CreateWorkoutUiState(
        isEditing = id != null
    )
) {
    init {
        findWorkout()

        observeLoading()
        observeSettings()
    }

    private fun findWorkout() = launchLoading(Dispatchers.IO) {
        if(!uiState.value.isEditing || id == null) return@launchLoading

        workoutRepository.findWorkoutById(id)?.let {
            updateState { state ->
                state.copy(
                    initialWorkout = it.toWorkoutResponse(),
                    workout = it.toWorkoutResponse()
                )
            }
        }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeSettings() = vmScope.launch {
        settingsRepository.findSettings()
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { settings ->
                updateState { it.copy(settings = settings) }
            }
    }

    private fun save(workoutName: String) = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        val workout = uiState.value.workout.apply {
            exercises.forEachIndexed { index, workoutExerciseResponse ->
                workoutExerciseResponse.order = index
            }
            name = workoutName
            unit = uiState.value.settings.unit
        }

        workoutRepository.saveWorkout(
            userId = userId,
            workout = workout,
            isUpdate = uiState.value.isEditing,
            callback = object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(CreateWorkoutUiEvent.NavigateBack)
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

    private fun reorderExercises(from: Int, to: Int) = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.toMutableList().apply {
                    val exercise = removeAt(from)
                    add(to, exercise)
                }
            )
        )
    }

    override fun onAction(action: CreateWorkoutUiAction) {

        when (action) {
            is CreateWorkoutUiAction.Save -> save(action.workoutName)
            is CreateWorkoutUiAction.OnExerciseNotesChanged -> updateState {
                it.copy(
                    workout = it.workout.copy(
                        exercises = it.workout.exercises.map { exercise ->
                            if (exercise.id == action.id) {
                                exercise.copy(notes = action.notes)
                            } else exercise
                        }
                    )
                )
            }
            is CreateWorkoutUiAction.OnDeleteExerciseClicked -> updateState {
                it.copy(
                    workout = it.workout.copy(
                        exercises = it.workout.exercises.filterIndexed { index, _ -> index != action.index }
                    ),
                )
            }
            is CreateWorkoutUiAction.OnAddExerciseClicked -> triggerEvent(CreateWorkoutUiEvent.NavigateToAddExercise)
            is CreateWorkoutUiAction.OnAddExercises -> updateState {
                val newExercises = action.exercises.map { exercise ->
                    WorkoutExerciseResponse(
                        exercise = exercise,
                        rest = it.settings.rest,
                        restEnabled = it.settings.restEnabled,
                    ).apply { sets = listOf(WorkoutSet(workoutExerciseId = this@apply.id)) }
                }

                it.copy(
                    workout = it.workout.copy(
                        exercises = it.workout.exercises + newExercises
                    ),
                )
            }
            is CreateWorkoutUiAction.OnReorderExercises -> updateState {
                it.copy(
                    workout = it.workout.copy(
                        exercises = action.exercises
                    )
                )
            }
            is CreateWorkoutUiAction.OnRestEnabledChanged -> updateState {
                it.copy(
                    workout = it.workout.copy(
                        exercises = it.workout.exercises.mapIndexed { index, exercise ->
                            if (index == action.exerciseIndex) exercise.copy(restEnabled = action.enabled) else exercise
                        }
                    )
                )
            }
            is CreateWorkoutUiAction.OnRestChanged -> updateState {
                it.copy(
                    workout = it.workout.copy(
                        exercises = it.workout.exercises.mapIndexed { index, exercise ->
                            if (index == action.exerciseIndex) exercise.copy(rest = action.rest) else exercise
                        }
                    )
                )
            }

            is CreateWorkoutUiAction.OnReorder -> reorderExercises(action.from, action.to)
        }
    }

    fun onWorkoutEvent(event: WorkoutExerciseEvent) {
        when (event) {
            is WorkoutExerciseEvent.OnRepsChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithRepsChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        reps = event.reps
                    )
                )
            }

            is WorkoutExerciseEvent.OnWeightChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithWeightChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        weight = event.weight
                    )
                )
            }

            is WorkoutExerciseEvent.OnTimeChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithTimeChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        time = event.time
                    )
                )
            }

            is WorkoutExerciseEvent.OnDeleteSetClicked -> updateState {
                it.copy(
                    workout = it.workout.copyWithDeleteSet(
                        id = event.id,
                        setIndex = event.setIndex
                    )
                )
            }

            is WorkoutExerciseEvent.OnAddSetClicked -> updateState {
                it.copy(
                    workout = it.workout.copyWithAddSet(
                        id = event.id,
                    )
                )
            }
        }
    }
}
