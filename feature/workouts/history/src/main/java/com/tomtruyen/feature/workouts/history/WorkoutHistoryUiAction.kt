package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiAction {
    sealed class Navigate: WorkoutHistoryUiAction() {
        data class Detail(val id: String): Navigate()
    }

    sealed class Workout: WorkoutHistoryUiAction() {
        data object Start: Workout()

        data object Save: Workout()
    }

    sealed class Sheet: WorkoutHistoryUiAction() {
        data class Show(val id: String): Sheet()

        data object Dismiss: Sheet()
    }

    data object Refresh : WorkoutHistoryUiAction()

    data class LoadMore(val page: Int) : WorkoutHistoryUiAction()
}