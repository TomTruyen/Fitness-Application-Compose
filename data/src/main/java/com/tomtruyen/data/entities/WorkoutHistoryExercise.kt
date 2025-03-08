package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.ExerciseType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@Entity(
    tableName = WorkoutHistoryExercise.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutHistory::class,
            parentColumns = ["id"],
            childColumns = ["workoutHistoryId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["exerciseId"]),
        androidx.room.Index(value = ["workoutHistoryId"])
    ]
)
data class WorkoutHistoryExercise(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_NAME)
    val name: String = "",
    @SerialName(KEY_IMAGE_URL)
    val imageUrl: String? = null,
    @SerialName(KEY_TYPE)
    val type: String = ExerciseType.WEIGHT.value,
    @SerialName(KEY_NOTES)
    val notes: String? = null,
    @SerialName(KEY_SORT_ORDER)
    val sortOrder: Int = 0,
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String? = null,
    @SerialName(KEY_WORKOUT_HISTORY_ID)
    val workoutHistoryId: String = "",
    @SerialName(KEY_CATEGORY)
    val category: String? = null,
    @SerialName(KEY_EQUIPMENT)
    val equipment: String? = null,
    @Transient
    override val synced: Boolean = true,
) : BaseEntity, SyncEntity {
    companion object {
        const val TABLE_NAME = "WorkoutHistoryExercise"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_IMAGE_URL = "image_url"
        const val KEY_TYPE = "type"
        const val KEY_NOTES = "notes"
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_WORKOUT_HISTORY_ID = "workout_history_id"
        const val KEY_CATEGORY = "category"
        const val KEY_EQUIPMENT = "equipment"
    }
}

data class WorkoutHistoryExerciseWithSets(
    @Embedded
    val workoutHistoryExercise: WorkoutHistoryExercise = WorkoutHistoryExercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutHistoryExerciseId",
        entity = WorkoutHistoryExerciseSet::class
    )
    val sets: List<WorkoutHistoryExerciseSet> = emptyList()
)