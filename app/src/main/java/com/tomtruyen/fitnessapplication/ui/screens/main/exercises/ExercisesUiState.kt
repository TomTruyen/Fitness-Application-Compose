package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.data.entities.Exercise

data class ExercisesUiState(
    val search: String = "",
    val searching: Boolean = false,
)
