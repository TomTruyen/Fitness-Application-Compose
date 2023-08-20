package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.networking.WorkoutResponse

data class CreateWorkoutUiState(
    val initialWorkout: WorkoutResponse = WorkoutResponse(),
    val workout: WorkoutResponse = WorkoutResponse(),
    val settings: Settings = Settings()
)

