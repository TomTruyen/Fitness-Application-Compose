package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutNavigationType
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.YearMonth

class ExecuteWorkoutViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: WorkoutHistoryRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExecuteWorkoutNavigationType>() {
    val state = MutableStateFlow(ExecuteWorkoutUiState())

    val lastEntryForWorkout = historyRepository.findLastEntryForWorkout(id)

    init {
        findWorkout()
        getLastEntryForWorkout()
    }

    private fun findWorkout() = launchIO {
        workoutRepository.findWorkoutById(id)?.let {
            state.value = state.value.copy(
                workout = it.toWorkoutResponse()
            )
        }
    }

    private fun getLastEntryForWorkout() = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

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

    private fun finishWorkout(duration: Long) = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        historyRepository.finishWorkout(
            userId = userId,
            history = WorkoutHistoryResponse(
                duration = duration,
                workout = state.value.workout
            ),
            callback = object: FirebaseCallback<List<WorkoutHistoryResponse>> {
                override fun onSuccess(value: List<WorkoutHistoryResponse>) {
                    navigate(ExecuteWorkoutNavigationType.Finish)
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

    fun onEvent(event: ExecuteWorkoutUiEvent) {
        when(event) {
            is ExecuteWorkoutUiEvent.NextExercise -> navigate(ExecuteWorkoutNavigationType.NextExercise)
            is ExecuteWorkoutUiEvent.FinishWorkout -> finishWorkout(event.duration)
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

            else -> Unit
        }
    }
}
