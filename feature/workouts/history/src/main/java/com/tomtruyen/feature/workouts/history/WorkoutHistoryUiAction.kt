package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    data class OnDetailClicked(val id: String) : WorkoutHistoryUiAction()
}