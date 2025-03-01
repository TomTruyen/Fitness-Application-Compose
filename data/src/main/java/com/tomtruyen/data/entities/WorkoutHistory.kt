package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.utils.TimeUtils
import java.util.UUID
import java.util.concurrent.TimeUnit

// TODO: At SerialName + KEY in companion object
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
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    val workoutId: String? = null,
    val duration: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
) : BaseEntity {
    val formattedDuration
        get() = TimeUtils.formatSeconds(
            seconds = duration,
            alwaysShow = listOf(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS),
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
    val totalWeight
        get(): Double {
            val weight = workoutWithExercises.exercises.sumOf {
                it.sets.sumOf setSumOf@{ set ->
                    val reps = set.reps ?: 0
                    val weight = set.weight ?: 0.0
                    reps * weight
                }
            }

            return weight
        }

    val weightUnit get() = workoutWithExercises.workout.unit

    val totalTime
        get() = workoutWithExercises.exercises.sumOf {
            it.sets.sumOf { set -> set.time ?: 0 }
        }
}