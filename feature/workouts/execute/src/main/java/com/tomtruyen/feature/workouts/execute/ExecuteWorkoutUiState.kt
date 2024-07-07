package com.tomtruyen.feature.workouts.execute

import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.firebase.models.WorkoutResponse

data class ExecuteWorkoutUiState(
    val workout: WorkoutResponse = WorkoutResponse(),
    val lastEntryForWorkout: WorkoutWithExercises? = null,

    val loading: Boolean = false,
)
