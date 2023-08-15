package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import com.tomtruyen.fitnessapplication.data.entities.Exercise

data class CreateExerciseUiState(
    val exercise: Exercise = Exercise(),
    val isEditing: Boolean = false,
)

