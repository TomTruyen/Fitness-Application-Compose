package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.ExerciseType
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
    @SerialName("user_id")
    val userId: String? = null,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Workout"
    }
}

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout = Workout(),
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutExercise::class
    )
    val exercises: List<WorkoutExerciseWithSets> = emptyList()
) {
    private val weightExercises: List<WorkoutExerciseWithSets>
        get() = exercises.filter {
            it.exercise.exercise.typeEnum == ExerciseType.WEIGHT
        }

    val totalWeightCompleted: Double
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.sumOf { set ->
                val reps = set.reps ?: 0
                val weight = set.weight ?: 0.0
                reps * weight
            }
        }

    val repsCountCompleted: Int
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.sumOf { set ->
                set.reps ?: 0
            }
        }

    val setsCountCompleted: Int
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.size
        }

    val totalVolumeCompleted: Double
        get() = repsCountCompleted * totalWeightCompleted

    fun copyWithRepsChanged(
        id: String,
        setIndex: Int,
        reps: String?
    ) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            reps = reps?.toIntOrNull(),
                        ) else set
                    }
                )
            } else workoutExercise
        }
    )

    fun copyWithWeightChanged(
        id: String,
        setIndex: Int,
        weight: String?
    ) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            weight = weight?.toDoubleOrNull(),
                        ) else set
                    }
                )
            } else workoutExercise
        }
    )

    fun copyWithTimeChanged(
        id: String,
        setIndex: Int,
        time: Int?
    ) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            time = time ?: 0,
                        ) else set
                    }
                )
            } else workoutExercise
        }
    )

    fun copyWithDeleteSet(
        id: String,
        setIndex: Int
    ) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets.filterIndexed { sIndex, _ -> sIndex != setIndex }
                )
            } else workoutExercise
        }
    )

    fun copyWithAddSet(id: String) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets + WorkoutExerciseSet(
                        workoutExerciseId = workoutExercise.workoutExercise.id,
                        sortOrder = workoutExercise.sets.lastOrNull()?.sortOrder?.plus(1) ?: 0
                    )
                )
            } else workoutExercise
        }
    )

    fun copyWithSetCompleted(id: String, setIndex: Int) = copy(
        exercises = exercises.map { workoutExercise ->
            if (workoutExercise.workoutExercise.id == id) {
                workoutExercise.copy(
                    sets = workoutExercise.sets.toMutableList().apply {
                        this[setIndex] = this[setIndex].copy(completed = !this[setIndex].completed)
                    }
                )
            } else workoutExercise
        }
    )
}
