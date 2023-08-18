package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewNavigationType {
    data object CreateWorkout : WorkoutOverviewNavigationType()
}
