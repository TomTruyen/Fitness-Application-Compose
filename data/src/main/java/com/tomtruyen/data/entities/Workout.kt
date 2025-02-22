package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.data.entities.BaseEntity.Companion.DEFAULT_TTL
import com.tomtruyen.data.firebase.models.WorkoutResponse
import java.util.UUID

@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override val ttl: Long = DEFAULT_TTL,
    val name: String = "",
    val unit: String = Settings.UnitType.KG.value, // KG or LBS
    val createdAt: Long = System.currentTimeMillis(),
    val isPerformed: Boolean = false,
): BaseEntity {
    companion object {
        const val TABLE_NAME = "workouts"
    }
}

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutExercise::class
    )
    val exercises: List<WorkoutExerciseWithExercisesAndSets> = emptyList()
) {
    fun toWorkoutResponse() = WorkoutResponse(
        id = workout.id,
        name = workout.name,
        unit = workout.unit,
        exercises = exercises.map { it.toWorkoutExerciseResponse() }.sortedBy { it.order },
        createdAt = workout.createdAt
    )
}
