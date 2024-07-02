package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

sealed class WorkoutHistoryUiAction {
    data class OnDetailClicked(val id: String) : WorkoutHistoryUiAction()
}