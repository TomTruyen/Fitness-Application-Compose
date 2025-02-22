package com.tomtruyen.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID
import com.tomtruyen.data.entities.BaseEntity.Companion.DEFAULT_TTL

@Entity(
    tableName = WorkoutSet.TABLE_NAME,
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
@Parcelize
data class WorkoutSet(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override val ttl: Long = DEFAULT_TTL,
    val workoutExerciseId: String? = null,
    val reps: Int = 0, // For Weight Type exercises
    val weight: Double = 0.0, // For Weight Type exercises
    val time: Int = 0, // For Time Type exercises,
    val order: Int = 1,
    val completed: Boolean = false,

    // These are just used for the UI to act as a buffer for the user input
    val inputWeight: String? = null,
    val inputReps: String? = null
): BaseEntity, Parcelable {
    companion object {
        const val TABLE_NAME = "workout_sets"
    }
}