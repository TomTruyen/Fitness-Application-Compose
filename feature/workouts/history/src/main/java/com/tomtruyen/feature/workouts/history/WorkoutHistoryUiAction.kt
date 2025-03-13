package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    sealed class Navigate: WorkoutHistoryUiAction() {
        data class Detail(val id: String): Navigate()
    }

    data object Refresh : WorkoutHistoryUiAction()

    data class LoadMore(val page: Int) : WorkoutHistoryUiAction()
}