package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

sealed class WorkoutHistoryNavigationType {
    data class Detail(val id: String) : WorkoutHistoryNavigationType()
}
