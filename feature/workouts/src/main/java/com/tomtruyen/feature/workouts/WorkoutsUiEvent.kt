package com.tomtruyen.feature.workouts

sealed class WorkoutsUiEvent {
    data object NavigateToCreateWorkout : WorkoutsUiEvent()

    data class NavigateToDetail(val id: String) : WorkoutsUiEvent()

    data class NavigateToStartWorkout(val id: String) : WorkoutsUiEvent()

}
