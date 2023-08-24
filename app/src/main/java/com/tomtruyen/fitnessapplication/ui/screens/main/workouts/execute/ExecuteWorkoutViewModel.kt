package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ExecuteWorkoutViewModel(
    id: String,
    workoutRepository: WorkoutRepository
): BaseViewModel<ExecuteWorkoutNavigationType>() {
    val state = MutableStateFlow(ExecuteWorkoutUiState())

    val workout = workoutRepository.findWorkoutByIdAsync(id)

    fun onEvent(event: ExecuteWorkoutUiEvent) {
        when(event) {
            else -> Unit
        }
    }
}
