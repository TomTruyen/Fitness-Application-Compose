package com.tomtruyen.fitnessapplication.networking

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import java.util.UUID

data class WorkoutResponse(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var exercises: List<WorkoutExerciseResponse> = emptyList()
)

data class WorkoutExerciseResponse(
    var id: String = UUID.randomUUID().toString(),
    var notes: String = "",
    var rest: Int = 0,
    var restEnabled: Boolean = false,
    var order: Int = 0,
    var exercise: Exercise = Exercise(),
    var sets: List<WorkoutSet> = emptyList()
)