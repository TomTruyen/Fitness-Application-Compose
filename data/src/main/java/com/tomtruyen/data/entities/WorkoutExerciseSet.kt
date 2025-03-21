package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tomtruyen.core.common.models.BaseSet
import com.tomtruyen.core.common.models.ChangeType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@Entity(
    tableName = WorkoutExerciseSet.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["workoutExerciseId"])
    ]
)
data class WorkoutExerciseSet(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_WORKOUT_EXERCISE_ID)
    val workoutExerciseId: String = "",
    @SerialName(KEY_REPS)
    override val reps: Int? = null,
    @SerialName(KEY_WEIGHT)
    override val weight: Double? = null,
    @SerialName(KEY_TIME)
    override val time: Int? = null,
    @SerialName(KEY_SORT_ORDER)
    override val sortOrder: Int = 0,
    @Transient
    val completed: Boolean = false,
    @Transient
    val changeRecord: Set<ChangeType> = emptySet(),
    @Transient
    override val synced: Boolean = false,
) : BaseEntity, SyncEntity, BaseSet {
    companion object {
        const val TABLE_NAME = "WorkoutExerciseSet"

        const val KEY_ID = "id"
        const val KEY_WORKOUT_EXERCISE_ID = "workout_exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"
        const val KEY_SORT_ORDER = "sort_order"
    }
}