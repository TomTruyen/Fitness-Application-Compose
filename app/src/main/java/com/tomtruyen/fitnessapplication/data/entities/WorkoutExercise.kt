package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(
    tableName = WorkoutExercise.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutExercise(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var workoutId: String = "",
    var exerciseId: String = "",
    var notes: String? = null,
    var rest: Int = 30,
    var restEnabled: Boolean = true,
    var order: Int = 0
) {
    companion object {
        const val TABLE_NAME = "workout_exercises"
    }
}

data class WorkoutExerciseWithExercisesAndSets(
    @Embedded
    val workoutExercise: WorkoutExercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id",
        entity = Exercise::class,
    )
    val exercise: Exercise,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutExerciseId",
        entity = WorkoutSet::class
    )
    val sets: List<WorkoutSet> = emptyList()
)
