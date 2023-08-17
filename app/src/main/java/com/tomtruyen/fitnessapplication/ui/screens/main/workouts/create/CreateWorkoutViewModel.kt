package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class CreateWorkoutViewModel: BaseViewModel<CreateWorkoutNavigationType>() {
    val state = MutableStateFlow(CreateWorkoutUiState())

    fun onEvent(event: CreateWorkoutUiEvent) {
        when(event) {
            else -> Unit
        }
    }
}
