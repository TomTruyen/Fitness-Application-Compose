package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    data object OnRefresh: WorkoutHistoryUiAction()
    data class OnLoadMore(val page: Int): WorkoutHistoryUiAction()
}