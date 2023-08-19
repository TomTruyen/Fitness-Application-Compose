package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse

data class ReorderWorkoutExercisesUiState(
    val exercises: List<WorkoutExerciseResponse> = emptyList()
)
