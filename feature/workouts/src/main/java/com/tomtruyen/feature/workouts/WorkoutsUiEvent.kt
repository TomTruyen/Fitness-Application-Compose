package com.tomtruyen.feature.workouts

sealed class WorkoutsUiEvent {
    data object NavigateToManageWorkout : WorkoutsUiEvent()

    data class NavigateToDetail(val id: String) : WorkoutsUiEvent()

    data class NavigateToStartWorkout(val id: String) : WorkoutsUiEvent()

}
