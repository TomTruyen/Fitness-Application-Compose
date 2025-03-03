package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = WorkoutHistoryExerciseSet.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = WorkoutHistoryExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutHistoryExerciseId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["workoutHistoryExerciseId"])
    ]
)
data class WorkoutHistoryExerciseSet(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_WORKOUT_HISTORY_EXERCISE_ID)
    val workoutHistoryExerciseId: String = "",
    @SerialName(KEY_REPS)
    val reps: Int? = null,
    @SerialName(KEY_WEIGHT)
    val weight: Double? = null,
    @SerialName(KEY_TIME)
    val time: Int? = null,
    @SerialName(KEY_SORT_ORDER)
    val sortOrder: Int = 0,
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String? = null,
    @SerialName(KEY_CREATED_AT)
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
): BaseEntity {
    companion object {
        const val TABLE_NAME = "WorkoutHistoryExerciseSet"

        const val KEY_ID = "id"
        const val KEY_WORKOUT_HISTORY_EXERCISE_ID = "workout_history_exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_CREATED_AT = "created_at"
    }
}