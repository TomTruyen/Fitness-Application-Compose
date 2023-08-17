package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var unit: String = Settings.UnitType.KG.value, // KG or LBS
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
)
