package com.tomtruyen.feature.exercises

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.navigation.Screen

data class ExercisesUiState(
    val mode: Screen.Exercise.Overview.Mode = Screen.Exercise.Overview.Mode.VIEW, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: ExerciseFilter = ExerciseFilter(),
    val selectedExercises: List<Exercise> = emptyList(),

    val exercises: List<Exercise> = emptyList(),
    val categories: List<Category> = emptyList(),
    val equipment: List<Equipment> = emptyList(),

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
