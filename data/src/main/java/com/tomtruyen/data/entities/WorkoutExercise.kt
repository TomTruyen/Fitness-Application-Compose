package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = WorkoutExercise.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index("workoutId"),
        androidx.room.Index("exerciseId")
    ]
)
data class WorkoutExercise(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String = "",
    @SerialName(KEY_WORKOUT_ID)
    val workoutId: String = "",
    @SerialName(KEY_NOTES)
    val notes: String? = null,
    @SerialName(KEY_SORT_ORDER)
    val sortOrder: Int = 0,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "WorkoutExercise"

        const val KEY_ID = "id"
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_WORKOUT_ID = "workout_id"
        const val KEY_NOTES = "notes"
        const val KEY_SORT_ORDER = "sort_order"
    }
}

data class WorkoutExerciseWithSets(
    @Embedded
    val workoutExercise: WorkoutExercise = WorkoutExercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutExerciseId",
        entity = WorkoutExerciseSet::class
    )
    val sets: List<WorkoutExerciseSet> = emptyList(),
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id",
        entity = Exercise::class
    )
    val exercise: ExerciseWithCategoryAndEquipment
)