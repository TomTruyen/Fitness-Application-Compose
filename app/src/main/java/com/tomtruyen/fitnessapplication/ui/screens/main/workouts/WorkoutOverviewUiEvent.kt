package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewUiEvent {
    data object NavigateToCreateWorkout : WorkoutOverviewUiEvent()
    data class NavigateToDetail(val id: String) : WorkoutOverviewUiEvent()
    data class NavigateToStartWorkout(val id: String) : WorkoutOverviewUiEvent()

}
