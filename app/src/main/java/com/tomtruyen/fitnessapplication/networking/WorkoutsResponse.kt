package com.tomtruyen.fitnessapplication.networking

import android.os.Parcelable
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Workout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutExercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import kotlinx.parcelize.Parcelize
import java.util.UUID

data class WorkoutsResponse(
    val data: List<WorkoutResponse> = emptyList()
)

@Parcelize
data class WorkoutResponse(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var unit: String = "",
    var exercises: List<WorkoutExerciseResponse> = emptyList(),
    var createdAt: Long = System.currentTimeMillis()
): Parcelable {
    fun toWorkout(): Workout = Workout(
        id = id,
        name = name,
        unit = unit,
        createdAt = createdAt
    )
}

@Parcelize

data class WorkoutExerciseResponse(
    var id: String = UUID.randomUUID().toString(),
    var notes: String = "",
    var rest: Int = 0,
    var restEnabled: Boolean = false,
    var order: Int = 0,
    var exercise: Exercise = Exercise(),
    var sets: List<WorkoutSet> = listOf()
): Parcelable {
    fun toWorkoutExercise(workoutId: String): WorkoutExercise = WorkoutExercise(
        id = id,
        notes = notes,
        rest = rest,
        restEnabled = restEnabled,
        order = order,
        workoutId = workoutId,
        exerciseId = exercise.id
    )
}