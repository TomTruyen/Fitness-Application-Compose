package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExecuteWorkoutViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: WorkoutHistoryRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExecuteWorkoutUiState, ExecuteWorkoutUiAction, ExecuteWorkoutUiEvent>(
    initialState = ExecuteWorkoutUiState()
) {
    val lastEntryForWorkout = historyRepository.findLastEntryForWorkout(id)

    init {
        findWorkout()
        getLastEntryForWorkout()
    }

    private fun findWorkout() = vmScope.launch {
        workoutRepository.findWorkoutById(id)?.let {
            updateState { state ->
                state.copy(
                    workout = it.toWorkoutResponse()
                )
            }
        }
    }

    private fun getLastEntryForWorkout() = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        historyRepository.getLastEntryForWorkout(
            userId = userId,
            workoutId = id,
            callback = object: FirebaseCallback<Unit> {
                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    private fun finishWorkout(duration: Long) = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        historyRepository.finishWorkout(
            userId = userId,
            history = WorkoutHistoryResponse(
                duration = duration,
                workout = uiState.value.workout
            ),
            callback = object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(ExecuteWorkoutUiEvent.NavigateToFinish)
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

    override fun onAction(action: ExecuteWorkoutUiAction) {
        when(action) {
            is ExecuteWorkoutUiAction.NextExercise -> triggerEvent(ExecuteWorkoutUiEvent.NavigateToNextExercise)
            is ExecuteWorkoutUiAction.FinishWorkout -> finishWorkout(action.duration)
        }
    }

    fun onWorkoutEvent(event: WorkoutExerciseEvent) {
        when (event) {
            is WorkoutExerciseEvent.OnRepsChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithRepsChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        reps = event.reps
                    )
                )
            }

            is WorkoutExerciseEvent.OnWeightChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithWeightChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        weight = event.weight
                    )
                )
            }

            is WorkoutExerciseEvent.OnTimeChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithTimeChanged(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex,
                        time = event.time
                    )
                )
            }

            is WorkoutExerciseEvent.OnDeleteSetClicked -> updateState {
                it.copy(
                    workout = it.workout.copyWithDeleteSet(
                        exerciseIndex = event.exerciseIndex,
                        setIndex = event.setIndex
                    )
                )
            }

            else -> Unit
        }
    }
}
