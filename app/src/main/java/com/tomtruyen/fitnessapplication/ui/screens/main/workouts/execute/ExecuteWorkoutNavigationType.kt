package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

sealed class ExecuteWorkoutNavigationType {
    data object NextExercise : ExecuteWorkoutNavigationType()
}
