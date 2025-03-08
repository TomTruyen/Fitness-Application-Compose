package com.tomtruyen.data.models.network

import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
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
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName(Workout.KEY_SORT_ORDER)
    val sortOrder: Int,
    @SerialName(Workout.KEY_USER_ID)
    val userId: String,
    @SerialName(WorkoutExercise.TABLE_NAME)
    val exercises: List<WorkoutExerciseNetworkModel>,
) {
    fun toEntity() = Workout(
        id = id,
        name = name,
        unit = unit,
        createdAt = createdAt,
        userId = userId,
        sortOrder = sortOrder
    )
}