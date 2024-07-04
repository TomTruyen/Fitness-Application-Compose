package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises

data class WorkoutDetailUiState(
    val workout: WorkoutWithExercises? = null,

    val loading: Boolean = false
)
