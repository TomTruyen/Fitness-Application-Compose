package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.WorkoutResponse

data class CreateWorkoutUiState(
    val isEditing: Boolean = false,
    val initialWorkout: WorkoutResponse = WorkoutResponse(),
    val workout: WorkoutResponse = WorkoutResponse(),
    val settings: com.tomtruyen.data.entities.Settings = com.tomtruyen.data.entities.Settings(),
    val selectedExerciseId: String? = null,

    val loading: Boolean = false,
)

