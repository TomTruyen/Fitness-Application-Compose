package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

sealed class WorkoutOverviewNavigationType {
    data object CreateWorkout : WorkoutOverviewNavigationType()
    data class Detail(val id: String) : WorkoutOverviewNavigationType()
    data class StartWorkout(val id: String) : WorkoutOverviewNavigationType()

}
