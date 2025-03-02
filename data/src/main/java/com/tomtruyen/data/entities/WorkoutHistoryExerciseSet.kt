package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
            onDelete = ForeignKey.CASCADE
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
): BaseEntity {
    companion object {
        const val TABLE_NAME = "WorkoutHistoryExerciseSet"

        const val KEY_ID = "id"
        const val KEY_WORKOUT_HISTORY_EXERCISE_ID = "workout_history_exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"
        const val KEY_SORT_ORDER = "sort_order"
    }
}