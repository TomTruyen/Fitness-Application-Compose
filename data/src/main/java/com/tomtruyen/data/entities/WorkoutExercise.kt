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
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index("workoutId"),
        androidx.room.Index("exerciseId")
    ]
)
data class WorkoutExercise(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("exercise_id")
    val exerciseId: String? = null,
    @SerialName("workout_id")
    val workoutId: String? = null,
    @SerialName("notes")
    val notes: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int = 0,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "WorkoutExercise"
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