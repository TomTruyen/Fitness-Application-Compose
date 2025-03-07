package com.tomtruyen.data.models.network.rpc

import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviousExerciseSet(
    @SerialName(KEY_ID)
    val id: String,
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String,
    @SerialName(KEY_REPS)
    val reps: Int?,
    @SerialName(KEY_WEIGHT)
    val weight: Double?,
    @SerialName(KEY_TIME)
    val time: Int?,
    @SerialName(KEY_SORT_ORDER)
    val sortOrder: Int,
    @SerialName(KEY_CREATED_AT)
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime,
) {
    companion object {
        const val KEY_ID = "id"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_CREATED_AT = "created_at"

        const val RPC_FUNCTION = "get_user_latest_sets_for_exercises"
        const val EXERCISE_ID_PARAM = "exercise_ids"
    }
}
