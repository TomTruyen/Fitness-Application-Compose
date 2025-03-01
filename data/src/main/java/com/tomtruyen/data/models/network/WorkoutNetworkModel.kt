package com.tomtruyen.data.models.network

import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutNetworkModel(
    @SerialName(Workout.KEY_ID)
    val id: String,
    @SerialName(Workout.KEY_NAME)
    val name: String,
    @SerialName(Workout.KEY_UNIT)
    val unit: String,
    @SerialName(Workout.KEY_CREATED_AT)
    val createdAt: LocalDateTime,
    @SerialName(Workout.KEY_USER_ID)
    val userId: String,
    @SerialName(WorkoutExercise.TABLE_NAME)
    val exercises: List<WorkoutExerciseNetworkModel>
)