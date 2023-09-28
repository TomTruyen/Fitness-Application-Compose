package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewUiEvent {
    data object OnCreateWorkoutClicked : WorkoutOverviewUiEvent()
    data class OnDetailClicked(val id: String) : WorkoutOverviewUiEvent()
    data class OnStartWorkoutClicked(val id: String) : WorkoutOverviewUiEvent()
}
