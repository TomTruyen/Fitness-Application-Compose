package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutWithExercises
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val unit: UnitType = UnitType.KG,
    val exercises: List<WorkoutExerciseUiModel> = emptyList(),
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

    fun toEntity(userId: String) = Workout(
        id = id,
        name = name,
        unit = unit.value,
        userId = userId,
    )

    fun toWorkoutHistoryEntity(userId: String, duration: Long) = WorkoutHistory(
        name = name,
        unit = unit.value,
        userId = userId,
        duration = duration
    )

    companion object {
        fun fromEntity(entity: WorkoutWithExercises) = WorkoutUiModel(
            id = entity.workout.id,
            name = entity.workout.name,
            unit = UnitType.fromValue(entity.workout.unit),
            exercises = entity.exercises.map(WorkoutExerciseUiModel::fromEntity).sortedBy { it.sortOrder }
        )
    }
}

// Extensions
fun WorkoutUiModel.copyWithRepsChanged(
    id: String,
    setIndex: Int,
    reps: String?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.mapIndexed { sIndex, set ->
                    if (sIndex == setIndex) set.copy(
                        reps = reps?.toIntOrNull(),
                    ) else set
                }
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyWithWeightChanged(
    id: String,
    setIndex: Int,
    weight: String?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.mapIndexed { sIndex, set ->
                    if (sIndex == setIndex) set.copy(
                        weight = weight?.toDoubleOrNull(),
                    ) else set
                }
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyWithTimeChanged(
    id: String,
    setIndex: Int,
    time: Int?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.mapIndexed { sIndex, set ->
                    if (sIndex == setIndex) set.copy(
                        time = time ?: 0,
                    ) else set
                }
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyWithDeleteSet(
    id: String,
    setIndex: Int
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.filterIndexed { sIndex, _ -> sIndex != setIndex }
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyWithAddSet(id: String) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets + WorkoutExerciseSetUiModel(
                    sortOrder = exercise.sets.lastOrNull()?.sortOrder?.plus(1) ?: 0
                )
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyWithSetCompleted(id: String, setIndex: Int) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.toMutableList().apply {
                    this[setIndex] = this[setIndex].copy(
                        completed = !this[setIndex].completed,
                        reps = if(exercise.type == ExerciseType.WEIGHT && this[setIndex].reps == null) 0 else this[setIndex].reps,
                        weight = if(exercise.type == ExerciseType.WEIGHT && this[setIndex].weight == null) 0.0 else this[setIndex].weight,
                        time = if(exercise.type == ExerciseType.TIME && this[setIndex].time == null) 0 else this[setIndex].time

                    )
                }
            )
        } else exercise
    }
)