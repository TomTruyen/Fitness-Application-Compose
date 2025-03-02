package com.tomtruyen.data.models.network

import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutHistoryNetworkModel(
    @SerialName(WorkoutHistory.KEY_ID)
    val id: String,
    @SerialName(WorkoutHistory.KEY_NAME)
    val name: String,
    @SerialName(WorkoutHistory.KEY_UNIT)
    val unit: String,
    @SerialName(WorkoutHistory.KEY_CREATED_AT)
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName(WorkoutHistory.KEY_USER_ID)
    val userId: String,
    @SerialName(WorkoutHistoryExercise.TABLE_NAME)
    val exercises: List<WorkoutHistoryExerciseNetworkModel>
)