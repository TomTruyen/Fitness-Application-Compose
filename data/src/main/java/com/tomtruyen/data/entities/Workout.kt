package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String = "",
    @SerialName("unit")
    val unit: String = Settings.UnitType.KG.value, // KG or LBS
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
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
)
