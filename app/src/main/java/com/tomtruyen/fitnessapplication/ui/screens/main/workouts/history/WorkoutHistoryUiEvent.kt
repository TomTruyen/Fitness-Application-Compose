package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

sealed class WorkoutHistoryUiEvent {
    data class OnDetailClicked(val id: String) : WorkoutHistoryUiEvent()
}