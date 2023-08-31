package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import java.util.UUID

@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var unit: String = Settings.UnitType.KG.value, // KG or LBS
    var createdAt: Long = System.currentTimeMillis()
) {
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
