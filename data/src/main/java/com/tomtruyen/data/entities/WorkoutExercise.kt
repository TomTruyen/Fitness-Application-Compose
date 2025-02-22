package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
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
    ],
    indices = [
        androidx.room.Index(value = ["workoutId"]),
        androidx.room.Index(value = ["exerciseId"])
    ]
)
data class WorkoutExercise(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    val workoutId: String? = null,
    val exerciseId: String? = null,
    val notes: String? = null,
    val rest: Int = 30,
    val restEnabled: Boolean = true,
    val order: Int = 0
): BaseEntity {
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
) {
    fun toWorkoutExerciseResponse() = WorkoutExerciseResponse(
        id = workoutExercise.id,
        notes = workoutExercise.notes.orEmpty(),
        rest = workoutExercise.rest,
        restEnabled = workoutExercise.restEnabled,
        order = workoutExercise.order,
        exercise = exercise,
        sets = sets
    )
}
