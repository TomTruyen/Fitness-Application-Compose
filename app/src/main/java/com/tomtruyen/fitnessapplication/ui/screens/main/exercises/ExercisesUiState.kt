package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.ExerciseFilter

data class ExercisesUiState(
    val isFromWorkout: Boolean = false, // Determines if the user is coming from the workout screen
    val search: String = "",
    val searching: Boolean = false,
    val filter: ExerciseFilter = ExerciseFilter(),
    val selectedExercise: Exercise? = null,
)
