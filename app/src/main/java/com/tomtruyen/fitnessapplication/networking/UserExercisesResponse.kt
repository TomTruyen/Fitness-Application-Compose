package com.tomtruyen.fitnessapplication.networking

import com.tomtruyen.fitnessapplication.data.entities.Exercise

data class UserExercisesResponse(
    val exercises: List<Exercise> = emptyList()
)