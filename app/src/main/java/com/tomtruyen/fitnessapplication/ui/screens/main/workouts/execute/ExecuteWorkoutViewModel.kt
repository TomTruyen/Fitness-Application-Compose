package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
import kotlinx.coroutines.flow.MutableStateFlow

class ExecuteWorkoutViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository
): BaseViewModel<ExecuteWorkoutNavigationType>() {
    val state = MutableStateFlow(ExecuteWorkoutUiState())

    init {
        findWorkout()
    }

    private fun findWorkout() = launchLoading {
        workoutRepository.findWorkoutById(id)?.let {
            state.value = state.value.copy(
                workout = it.toWorkoutResponse()
            )
        }
    }

    private fun finishWorkout() {
        // TODO: Logic to save workout to database
        // TODO: Should save to collection <userId>_<month>_<year>_history
        // TODO: It should also save the "workout" object in the history object --> In case workout gets deleted + to be able to use the ID of the workout to check for previous values
    }

    fun onEvent(event: ExecuteWorkoutUiEvent) {
        when(event) {
            is ExecuteWorkoutUiEvent.NextExercise -> navigate(ExecuteWorkoutNavigationType.NextExercise)
            is ExecuteWorkoutUiEvent.FinishWorkout -> finishWorkout()
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
