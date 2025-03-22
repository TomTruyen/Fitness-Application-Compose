package com.tomtruyen.data.entities

import androidx.room.Entity
import com.tomtruyen.core.common.models.BaseSet
import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
import com.tomtruyen.core.common.utils.CacheKeyHelper
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = PreviousSet.TABLE_NAME,
    primaryKeys = ["exerciseId", "sortOrder"]
)
data class PreviousSet(
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String,
    @SerialName(KEY_SORT_ORDER)
    override val sortOrder: Int,
    @SerialName(KEY_REPS)
    override val reps: Int?,
    @SerialName(KEY_WEIGHT)
    override val weight: Double?,
    @SerialName(KEY_TIME)
    override val time: Int?,
    @SerialName(KEY_CREATED_AT)
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime,
): BaseSet {
    companion object: CacheKeyHelper(PreviousSet.TABLE_NAME) {
        const val KEY_ID = "id"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_CREATED_AT = "created_at"

        const val RPC_FUNCTION = "get_user_latest_sets_for_exercises"
        const val EXERCISE_ID_PARAM = "exercise_ids"

        const val TABLE_NAME = "PreviousSet"

        fun fromWorkoutExerciseSet(exerciseId: String, set: WorkoutHistoryExerciseSet) =
            PreviousSet(
                exerciseId = exerciseId,
                sortOrder = set.sortOrder,
                reps = set.reps,
                weight = set.weight,
                time = set.time,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
    }
}