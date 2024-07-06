package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.data.entities.Exercise

data class ExerciseDetailUiState(
    val exercise: com.tomtruyen.data.entities.Exercise? = null,

    val loading: Boolean = false,
)
