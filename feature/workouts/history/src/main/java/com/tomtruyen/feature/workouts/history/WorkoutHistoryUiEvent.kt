package com.tomtruyen.feature.workouts.history

sealed class WorkoutHistoryUiEvent {
    data class NavigateToDetail(val id: String) : WorkoutHistoryUiEvent()
}
