package com.tomtruyen.feature.exercises

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.models.ExerciseFilter
import com.tomtruyen.navigation.Screen

data class ExercisesUiState(
    val mode: Screen.Exercise.Overview.Mode = Screen.Exercise.Overview.Mode.VIEW, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: ExerciseFilter = ExerciseFilter(),
    val selectedExercises: List<Exercise> = emptyList(),

    val exercises: List<Exercise> = emptyList(),
    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),

    val loading: Boolean = false,
)
