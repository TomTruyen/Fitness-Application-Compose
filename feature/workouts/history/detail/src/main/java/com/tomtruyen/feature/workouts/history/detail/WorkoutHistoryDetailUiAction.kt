package com.tomtruyen.feature.workouts.history.detail

sealed class WorkoutHistoryDetailUiAction {
    sealed class Navigate: WorkoutHistoryDetailUiAction() {
        sealed class Exercise: Navigate() {
            data class Detail(val id: String?): Exercise()
        }
    }

    sealed class Sheet: WorkoutHistoryDetailUiAction() {
        data object Show: Sheet()

        data object Dismiss: Sheet()
    }

    sealed class Workout: WorkoutHistoryDetailUiAction() {
        data object Start: Workout()

        data object Save: Workout()
    }
}