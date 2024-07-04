package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises

data class WorkoutOverviewUiState(
    val workouts: List<WorkoutWithExercises> = emptyList(),
    val loading: Boolean = false
)
