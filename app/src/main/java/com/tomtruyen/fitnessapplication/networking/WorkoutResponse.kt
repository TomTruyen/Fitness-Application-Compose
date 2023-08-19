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
    val id: String = UUID.randomUUID().toString(),
    val notes: String = "",
    val rest: Int = 0,
    val restEnabled: Boolean = false,
    val order: Int = 0,
    val exercise: Exercise = Exercise(),
    val sets: List<WorkoutSet> = emptyList()
)