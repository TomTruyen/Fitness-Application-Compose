package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

import com.tomtruyen.data.entities.WorkoutWithExercises

data class WorkoutDetailUiState(
    val workout: com.tomtruyen.data.entities.WorkoutWithExercises? = null,

    val loading: Boolean = false
)
