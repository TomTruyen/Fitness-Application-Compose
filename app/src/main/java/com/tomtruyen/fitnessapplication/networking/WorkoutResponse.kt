package com.tomtruyen.fitnessapplication.networking

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import java.util.UUID

data class WorkoutResponse(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val exercises: List<WorkoutExerciseResponse> = emptyList()
)

data class WorkoutExerciseResponse(
    val id: String,
    val notes: String,
    val rest: Int,
    val restEnabled: Boolean,
    val order: Int,
    val exercise: Exercise,
    val sets: List<WorkoutSet>
)