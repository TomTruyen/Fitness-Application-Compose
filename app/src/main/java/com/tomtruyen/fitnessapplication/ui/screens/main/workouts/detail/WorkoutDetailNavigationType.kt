package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

sealed class WorkoutDetailNavigationType {
    data class Edit(val id: String): WorkoutDetailNavigationType()
    data object Back: WorkoutDetailNavigationType()
    data class StartWorkout(val id: String): WorkoutDetailNavigationType()
}
