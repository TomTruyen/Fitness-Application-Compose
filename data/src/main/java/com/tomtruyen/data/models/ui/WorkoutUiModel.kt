package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.ChangeType
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.entities.PreviousSet
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val unit: UnitType = UnitType.KG,
    val exercises: List<WorkoutExerciseUiModel> = emptyList(),
    val sortOrder: Int = 0,
    val userId: String? = null,
    val duration: Long = 0L
) {
    private val weightExercises: List<WorkoutExerciseUiModel>
        get() = exercises.filter {
            it.type == ExerciseType.WEIGHT
        }

    val totalVolumeCompleted: Double
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.filter(WorkoutExerciseSetUiModel::completed).sumOf { set ->
                val reps = set.reps ?: 0
                val weight = set.weight ?: 0.0
                reps * weight
            }
        }

    val repsCountCompleted: Int
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.filter(WorkoutExerciseSetUiModel::completed).sumOf { set ->
                set.reps ?: 0
            }
        }

    val setsCountCompleted: Int
        get() = weightExercises.sumOf { exercise ->
            exercise.sets.filter(WorkoutExerciseSetUiModel::completed).size
        }

    fun toEntity(userId: String? = null) = Workout(
        id = id,
        name = name,
        unit = unit.value,
        userId = userId ?: this.userId,
        sortOrder = sortOrder,
        synced = false,
        duration = duration
    )

    fun toWorkoutHistoryEntity(userId: String) = WorkoutHistory(
        name = name,
        unit = unit.value,
        userId = userId,
        duration = duration,
        synced = false,
    )

    companion object {
        fun fromEntity(entity: WorkoutWithExercises) = WorkoutUiModel(
            id = entity.workout.id,
            name = entity.workout.name,
            unit = UnitType.fromValue(entity.workout.unit),
            exercises = entity.exercises.map(WorkoutExerciseUiModel::fromEntity)
                .sortedBy { it.sortOrder },
            sortOrder = entity.workout.sortOrder,
            userId = entity.workout.userId,
            duration = entity.workout.duration
        )
    }

    // Equals check that ignores "duration"
    fun isOriginalWorkout(other: WorkoutUiModel): Boolean {
        return this.exercises.size == other.exercises.size &&
                this.exercises.zip(other.exercises).all { (thisExercise, otherExercise) ->
                    thisExercise.isOriginalExercise(otherExercise)
                } &&
                this.sortOrder == other.sortOrder
    }
}