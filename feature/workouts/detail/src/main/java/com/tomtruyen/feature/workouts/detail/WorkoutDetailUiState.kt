package com.tomtruyen.feature.workouts.detail

import com.tomtruyen.data.entities.WorkoutWithExercises

data class WorkoutDetailUiState(
    val workout: WorkoutWithExercises? = null,

    val loading: Boolean = false
)
