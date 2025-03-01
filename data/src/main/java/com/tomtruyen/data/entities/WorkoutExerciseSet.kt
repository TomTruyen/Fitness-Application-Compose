package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = WorkoutExerciseSet.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExercise::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["workoutExerciseId"])
    ]
)
data class WorkoutExerciseSet(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("workout_exercise_id")
    val workoutExerciseId: String? = null,
    @SerialName("reps")
    val reps: Int? = null,
    @SerialName("weight")
    val weight: Double? = null,
    @SerialName("time")
    val time: Int? = null,
    @SerialName("sort_order")
    val sortOrder: Int = 0,

    // TODO: Remove these extra fields since I use the WorkoutExerciseSetUiModel now
    val completed: Boolean = false
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "WorkoutExerciseSet"
    }
}