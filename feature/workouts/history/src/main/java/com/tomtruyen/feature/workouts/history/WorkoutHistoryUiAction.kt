package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    data object Refresh : WorkoutHistoryUiAction()

    data class LoadMore(val page: Int) : WorkoutHistoryUiAction()
}