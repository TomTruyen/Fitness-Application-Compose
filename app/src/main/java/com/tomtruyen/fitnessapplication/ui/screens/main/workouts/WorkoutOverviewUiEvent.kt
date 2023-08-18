package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewUiEvent {
    data object OnCreateWorkoutClicked : WorkoutOverviewUiEvent()
}
