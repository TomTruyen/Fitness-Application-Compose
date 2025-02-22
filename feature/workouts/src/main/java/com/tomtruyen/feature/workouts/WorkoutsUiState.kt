package com.tomtruyen.feature.workouts

import com.tomtruyen.data.entities.WorkoutWithExercises

data class WorkoutsUiState(
    val workouts: List<WorkoutWithExercises> = emptyList(),

    val loading: Boolean = false,
    val refreshing: Boolean = false
)
