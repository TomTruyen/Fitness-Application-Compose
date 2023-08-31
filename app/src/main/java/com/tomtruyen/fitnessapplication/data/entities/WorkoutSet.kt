package com.tomtruyen.fitnessapplication.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

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
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutExerciseId: String? = null,
    var reps: Int? = null, // For Weight Type exercises
    var weight: Double? = null, // For Weight Type exercises
    var time: Int? = null, // For Time Type exercises,
    var order: Int = 1,
    var repsText: String? = null,
    var weightText: String? = null
): Parcelable {


    companion object {
        const val TABLE_NAME = "workout_sets"
    }
}