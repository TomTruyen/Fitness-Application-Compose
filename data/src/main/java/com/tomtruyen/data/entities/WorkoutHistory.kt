package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.data.firebase.models.WorkoutHistoryResponse
import java.util.UUID
import java.util.concurrent.TimeUnit

@Entity(
    tableName = WorkoutHistory.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["workoutId"])
    ]
)
data class WorkoutHistory(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutId: String = "",
    var duration: Long = 0L,
    var createdAt: Long = System.currentTimeMillis(),
) {
    val formattedDuration get() = TimeUtils.formatSeconds(
        seconds = duration,
        alwaysShow = listOf(TimeUnit.HOURS , TimeUnit.MINUTES, TimeUnit.SECONDS),
    )

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
    val workoutWithExercises: WorkoutWithExercises
) {
    val totalWeight get(): Double {
        val weight = workoutWithExercises.exercises.sumOf {
            it.sets.sumOf setSumOf@ { set ->
                val reps = set.reps ?: return@setSumOf 0.0
                val weight = set.weight ?: return@setSumOf 0.0
                reps * weight
            }
        }

        return weight
    }

    val weightUnit get() = workoutWithExercises.workout.unit

    val totalTime get() = workoutWithExercises.exercises.sumOf {
        it.sets.sumOf { set -> set.time ?: 0 }
    }

    fun toWorkoutHistoryResponse() = WorkoutHistoryResponse(
        id = history.id,
        duration = history.duration,
        createdAt = history.createdAt,
        workout = workoutWithExercises.toWorkoutResponse()
    )
}