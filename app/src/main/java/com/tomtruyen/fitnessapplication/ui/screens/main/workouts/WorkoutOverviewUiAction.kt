package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewUiAction {
    data object OnCreateWorkoutClicked : WorkoutOverviewUiAction()
    data class OnDetailClicked(val id: String) : WorkoutOverviewUiAction()
    data class OnStartWorkoutClicked(val id: String) : WorkoutOverviewUiAction()
}
