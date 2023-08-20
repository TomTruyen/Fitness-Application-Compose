package com.tomtruyen.fitnessapplication.networking

import com.google.gson.annotations.SerializedName
import com.tomtruyen.fitnessapplication.data.entities.Exercise

data class ExercisesResponse(
    val data: List<Exercise> = emptyList()
)