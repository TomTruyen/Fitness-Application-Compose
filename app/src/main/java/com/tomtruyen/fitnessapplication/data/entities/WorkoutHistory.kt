package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import java.util.UUID

@Entity(
    tableName = WorkoutHistory.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutHistory(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutId: String = "",
    var duration: Long = 0L,
    var createdAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val TABLE_NAME = "workout_history"
    }
}

data class WorkoutHistoryWithWorkout(
    @Embedded
    val history: WorkoutHistory,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "id",
        entity = Workout::class
    )
    val workout: WorkoutWithExercises
) {
    fun toWorkoutHistoryResponse() = WorkoutHistoryResponse(
        id = history.id,
        duration = history.duration,
        createdAt = history.createdAt,
        workout = workout.toWorkoutResponse()
    )
}