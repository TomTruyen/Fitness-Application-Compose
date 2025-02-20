package com.tomtruyen.feature.exercises

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.models.ExerciseFilter

data class ExercisesUiState(
    val isFromWorkout: Boolean = false, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: ExerciseFilter = ExerciseFilter(),
    val selectedExercises: List<Exercise> = emptyList(),

    val exercises: List<Exercise> = emptyList(),
    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),

    val loading: Boolean = false,
)
