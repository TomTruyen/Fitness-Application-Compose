package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.models.ExerciseFilter

data class ExercisesUiState(
    val isFromWorkout: Boolean = false, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: com.tomtruyen.models.ExerciseFilter = com.tomtruyen.models.ExerciseFilter(),
    val selectedExercise: com.tomtruyen.data.entities.Exercise? = null,

    val exercises: List<com.tomtruyen.data.entities.Exercise> = emptyList(),
    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),

    val loading: Boolean = false,
)
