package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

sealed class WorkoutHistoryUiEvent {
    data class NavigateToDetail(val id: String) : WorkoutHistoryUiEvent()
}
