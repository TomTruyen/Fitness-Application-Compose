package com.tomtruyen.feature.workouts.create.reorder

import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

data class ReorderWorkoutExercisesUiState(
    val exercises: List<WorkoutExerciseResponse> = emptyList()
)
