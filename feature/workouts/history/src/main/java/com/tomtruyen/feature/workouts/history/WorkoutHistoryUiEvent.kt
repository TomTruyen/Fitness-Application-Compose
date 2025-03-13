package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiEvent {
    sealed class Navigate: WorkoutHistoryUiEvent() {
        data class Detail(val id: String): Navigate()
    }
}
