package com.tomtruyen.data.firebase.models

import android.os.Parcelable
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutSet
import kotlinx.parcelize.Parcelize
import java.util.UUID

data class WorkoutsResponse(
    val data: List<WorkoutResponse> = emptyList()
)

@Parcelize
data class WorkoutResponse(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var unit: String = "",
    var exercises: List<WorkoutExerciseResponse> = emptyList(),
    var createdAt: Long = System.currentTimeMillis(),
): Parcelable {
    fun toWorkout(): Workout = Workout(
        id = id,
        name = name,
        unit = unit,
        createdAt = createdAt,
    )

    fun copyWithRepsChanged(
        id: String,
        setIndex: Int,
        reps: String?
    ) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            reps = reps?.toIntOrNull() ?: 0,
                            inputReps = reps
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithWeightChanged(
        id: String,
        setIndex: Int,
        weight: String?
    ) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            weight = weight?.toDoubleOrNull() ?: 0.0,
                            inputWeight = weight
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithTimeChanged(
        id: String,
        setIndex: Int,
        time: Int?
    ) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            time = time ?: 0,
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithDeleteSet(
        id: String,
        setIndex: Int
    ) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets.filterIndexed { sIndex, _ -> sIndex != setIndex }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithAddSet(id: String) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets + WorkoutSet(
                        workoutExerciseId = exercise.id,
                        order = exercise.sets.lastOrNull()?.order?.plus(1) ?: 0
                    )
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithSetCompleted(id: String, setIndex: Int) = copy(exercises = exercises.map { exercise ->
            if (exercise.id == id) {
                exercise.copy(
                    sets = exercise.sets.toMutableList().apply {
                        this[setIndex] = this[setIndex].copy(completed = !this[setIndex].completed)
                    }
                )
            } else {
                exercise
            }
        }
    )
}

@Parcelize
data class WorkoutExerciseResponse(
    var id: String = UUID.randomUUID().toString(),
    var notes: String = "",
    var rest: Int = 0,
    var restEnabled: Boolean = false,
    var order: Int = 0,
    var exercise: Exercise = Exercise(),
    var sets: List<WorkoutSet> = listOf()
): Parcelable {
    fun toWorkoutExercise(workoutId: String): WorkoutExercise =
        WorkoutExercise(
            id = id,
            notes = notes,
            rest = rest,
            restEnabled = restEnabled,
            order = order,
            workoutId = workoutId,
            exerciseId = exercise.id
        )
}