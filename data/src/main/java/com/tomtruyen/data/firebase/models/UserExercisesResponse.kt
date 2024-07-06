package com.tomtruyen.data.firebase.models

import com.tomtruyen.data.entities.Exercise

data class UserExercisesResponse(
    val exercises: List<com.tomtruyen.data.entities.Exercise> = emptyList()
)