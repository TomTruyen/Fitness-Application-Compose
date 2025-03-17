package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
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

    // Equals check that ignores "duration"
    fun isOriginalWorkout(other: WorkoutUiModel): Boolean {
        return this.exercises.size == other.exercises.size &&
                this.exercises.zip(other.exercises).all { (thisExercise, otherExercise) ->
                    thisExercise.isOriginalExercise(otherExercise)
                } &&
                this.sortOrder == other.sortOrder
    }
}