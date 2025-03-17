package com.tomtruyen.data.models.extensions

import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.ChangeType
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import java.util.UUID

fun WorkoutUiModel.copyWithRepsChanged(
    id: String,
    setIndex: Int,
    reps: String?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
                this[setIndex] = this[setIndex].copy(
                    reps = reps?.toIntOrNull(),
                    changeRecord = this[setIndex].changeRecord + ChangeType.REP
                )
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithWeightChanged(
    id: String,
    setIndex: Int,
    weight: String?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
                this[setIndex] = this[setIndex].copy(
                    weight = weight?.toDoubleOrNull(),
                    changeRecord = this[setIndex].changeRecord + ChangeType.WEIGHT
                )
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithTimeChanged(
    id: String,
    setIndex: Int,
    time: Int?
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
                this[setIndex] = this[setIndex].copy(
                    time = time,
                    changeRecord = this[setIndex].changeRecord + ChangeType.TIME
                )
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithDeleteSet(
    id: String,
    setIndex: Int
) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
                removeAt(setIndex)
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithAddSet(id: String) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets + WorkoutExerciseSetUiModel(
                sortOrder = exercise.sets.lastOrNull()?.sortOrder?.plus(1) ?: 0
            )

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithSetCompleted(id: String, setIndex: Int, previousSet: PreviousSet?) = copy(
    exercises = exercises.map { exercise ->
        if (exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
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
                    changeRecord = ChangeType.entries.toSet()
                )
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
    }
)

fun WorkoutUiModel.copyWithPreviousExerciseSet(id: String, setIndex: Int, previousSet: PreviousSet) = copy(
    exercises = exercises.map { exercise ->
        if(exercise.id == id) {
            val sets = exercise.sets.toMutableList().apply {
                this[setIndex] = this[setIndex].copy(
                    reps = if (exercise.type == ExerciseType.WEIGHT && previousSet.reps != null) {
                        previousSet.reps
                    } else {
                        this[setIndex].reps
                    },
                    weight = if (exercise.type == ExerciseType.WEIGHT && previousSet.weight != null) {
                        previousSet.weight
                    } else {
                        this[setIndex].weight
                    },
                    time = if (exercise.type == ExerciseType.TIME && previousSet.time != null) {
                        previousSet.time
                    } else {
                        this[setIndex].time
                    },
                    changeRecord = ChangeType.entries.toSet()
                )
            }

            return@map exercise.copy(sets = sets)
        }

        return@map exercise
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