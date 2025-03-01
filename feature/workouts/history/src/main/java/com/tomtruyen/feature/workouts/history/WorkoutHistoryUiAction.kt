package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    data object OnRefresh: WorkoutHistoryUiAction()
}