package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse

data class ExecuteWorkoutUiState(
    val workout: WorkoutResponse = WorkoutResponse(),
    val lastEntryForWorkout: WorkoutWithExercises? = null,

    val loading: Boolean = false,
)
