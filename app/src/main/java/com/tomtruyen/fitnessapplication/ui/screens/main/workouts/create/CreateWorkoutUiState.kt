package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.networking.WorkoutResponse

data class CreateWorkoutUiState(
    val workout: WorkoutResponse = WorkoutResponse()
)

