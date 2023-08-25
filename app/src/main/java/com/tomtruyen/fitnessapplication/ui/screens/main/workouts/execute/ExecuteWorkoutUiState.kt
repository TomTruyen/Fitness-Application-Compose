package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.networking.WorkoutResponse

data class ExecuteWorkoutUiState(
    val workout: WorkoutResponse = WorkoutResponse(),
)
