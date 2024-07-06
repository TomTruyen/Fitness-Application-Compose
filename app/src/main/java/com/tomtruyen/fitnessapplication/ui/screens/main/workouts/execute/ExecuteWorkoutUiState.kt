package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.firebase.models.WorkoutResponse

data class ExecuteWorkoutUiState(
    val workout: WorkoutResponse = WorkoutResponse(),
    val lastEntryForWorkout: com.tomtruyen.data.entities.WorkoutWithExercises? = null,

    val loading: Boolean = false,
)
