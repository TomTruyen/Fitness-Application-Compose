package com.tomtruyen.fitnessapplication.networking

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet

data class WorkoutResponse(
    val id: String,
    val name: String,
    val exercises: List<WorkoutExerciseResponse>
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