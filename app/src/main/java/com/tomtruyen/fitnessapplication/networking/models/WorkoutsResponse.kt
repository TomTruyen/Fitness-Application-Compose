package com.tomtruyen.fitnessapplication.networking.models

import android.os.Parcelable
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Workout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutExercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
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
    var createdAt: Long = System.currentTimeMillis()
): Parcelable {
    fun toWorkout(): Workout = Workout(
        id = id,
        name = name,
        unit = unit,
        createdAt = createdAt
    )

    fun copyWithRepsChanged(
        exerciseIndex: Int,
        setIndex: Int,
        reps: String?
    ) = copy(exercises = exercises.mapIndexed { eIndex, exercise ->
            if (eIndex == exerciseIndex) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            reps = reps?.toIntOrNull(),
                            repsText = reps
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithWeightChanged(
        exerciseIndex: Int,
        setIndex: Int,
        weight: String?
    ) = copy(exercises = exercises.mapIndexed { eIndex, exercise ->
            if (eIndex == exerciseIndex) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            weight = weight?.toDoubleOrNull(),
                            weightText = weight
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithTimeChanged(
        exerciseIndex: Int,
        setIndex: Int,
        time: Int?
    ) = copy(exercises = exercises.mapIndexed { eIndex, exercise ->
            if (eIndex == exerciseIndex) {
                exercise.copy(
                    sets = exercise.sets.mapIndexed { sIndex, set ->
                        if (sIndex == setIndex) set.copy(
                            time = time,
                        ) else set
                    }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithDeleteSet(
        exerciseIndex: Int,
        setIndex: Int
    ) = copy(exercises = exercises.mapIndexed { eIndex, exercise ->
            if (eIndex == exerciseIndex) {
                exercise.copy(
                    sets = exercise.sets.filterIndexed { sIndex, _ -> sIndex != setIndex }
                )
            } else {
                exercise
            }
        }
    )

    fun copyWithAddSet(
        exerciseIndex: Int
    ) = copy(exercises = exercises.mapIndexed { eIndex, exercise ->
            if (eIndex == exerciseIndex) {
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
    fun toWorkoutExercise(workoutId: String): WorkoutExercise = WorkoutExercise(
        id = id,
        notes = notes,
        rest = rest,
        restEnabled = restEnabled,
        order = order,
        workoutId = workoutId,
        exerciseId = exercise.id
    )
}