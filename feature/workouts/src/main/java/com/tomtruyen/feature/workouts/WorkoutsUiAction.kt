package com.tomtruyen.feature.workouts

sealed class WorkoutsUiAction {
    data object OnCreateWorkoutClicked : WorkoutsUiAction()

    data class OnDetailClicked(val id: String) : WorkoutsUiAction()

    data class OnStartWorkoutClicked(val id: String) : WorkoutsUiAction()

    data object OnRefresh : WorkoutsUiAction()
}
