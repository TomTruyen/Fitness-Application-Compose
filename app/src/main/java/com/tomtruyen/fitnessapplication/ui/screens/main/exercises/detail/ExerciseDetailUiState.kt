package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.fitnessapplication.data.entities.Exercise

data class ExerciseDetailUiState(
    val exercise: Exercise? = null,

    val loading: Boolean = false,
)
