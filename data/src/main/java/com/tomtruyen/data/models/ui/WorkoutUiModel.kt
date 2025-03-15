package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.ChangeType
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.models.network.rpc.PreviousExerciseSet
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
                        changeRecord = set.changeRecord.toMutableList().apply {
                            if(!set.changeRecord.contains(ChangeType.REP)) add(ChangeType.REP)
                        }
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
                        changeRecord = set.changeRecord.toMutableList().apply {
                            if(!set.changeRecord.contains(ChangeType.WEIGHT)) add(ChangeType.WEIGHT)
                        }
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
                        changeRecord = set.changeRecord.toMutableList().apply {
                            if(!set.changeRecord.contains(ChangeType.TIME)) add(ChangeType.TIME)
                        }
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

fun WorkoutUiModel.copyWithSetCompleted(id: String, setIndex: Int, previousSet: PreviousExerciseSet?) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            exercise.copy(
                sets = exercise.sets.toMutableList().apply {
                    this[setIndex] = this[setIndex].copy(
                        completed = !this[setIndex].completed,
                        reps = if (exercise.type == ExerciseType.WEIGHT && this[setIndex].reps == null) {
                            previousSet?.reps ?: 0
                        } else {
                            this[setIndex].reps
                        },
                        weight = if (exercise.type == ExerciseType.WEIGHT && this[setIndex].weight == null) {
                            previousSet?.weight ?: 0.0
                        } else {
                            this[setIndex].weight
                        },
                        time = if (exercise.type == ExerciseType.TIME && this[setIndex].time == null) {
                            previousSet?.time ?: 0
                        } else {
                            this[setIndex].time
                        },
                        changeRecord = ChangeType.entries
                    )
                }
            )
        } else exercise
    }
)

fun WorkoutUiModel.copyFromActiveWorkout(id: String) = copy(
    id = id,
    exercises = exercises.map { exercise ->
        val exerciseId = if(exercise.id.startsWith(Workout.ACTIVE_WORKOUT_ID)) {
            UUID.randomUUID().toString()
        } else exercise.id

        exercise.copy(
            id = exerciseId,
            sets = exercise.sets.map { set ->
                val setId = if(set.id.startsWith(Workout.ACTIVE_WORKOUT_ID)) {
                    UUID.randomUUID().toString()
                } else set.id

                set.copy(
                    id = setId,
                    exerciseId = exerciseId,
                    completed = false
                )
            }
        )
    }
)