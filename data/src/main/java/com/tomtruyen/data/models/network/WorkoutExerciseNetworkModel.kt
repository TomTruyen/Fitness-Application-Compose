package com.tomtruyen.data.models.network

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExerciseNetworkModel(
    @SerialName(WorkoutExercise.KEY_ID)
    val id: String,
    @SerialName(WorkoutExercise.KEY_EXERCISE_ID)
    val exerciseId: String?,
    @SerialName(WorkoutExercise.KEY_WORKOUT_ID)
    val workoutId: String?,
    @SerialName(WorkoutExercise.KEY_NOTES)
    val notes: String?,
    @SerialName(WorkoutExercise.KEY_SORT_ORDER)
    val sortOrder: Int,
    @SerialName(Exercise.TABLE_NAME)
    val exercise: ExerciseNetworkModel,
    @SerialName(WorkoutExerciseSet.TABLE_NAME)
    val sets: List<WorkoutExerciseSet>
)