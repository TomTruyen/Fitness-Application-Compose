package com.tomtruyen.data.firebase.models

import com.tomtruyen.data.entities.Exercise

data class ExercisesResponse(
    val data: List<Exercise> = emptyList()
)