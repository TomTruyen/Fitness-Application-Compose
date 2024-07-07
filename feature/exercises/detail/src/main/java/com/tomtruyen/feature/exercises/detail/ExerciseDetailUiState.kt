package com.tomtruyen.feature.exercises.detail

import com.tomtruyen.data.entities.Exercise

data class ExerciseDetailUiState(
    val exercise: Exercise? = null,

    val loading: Boolean = false,
)
