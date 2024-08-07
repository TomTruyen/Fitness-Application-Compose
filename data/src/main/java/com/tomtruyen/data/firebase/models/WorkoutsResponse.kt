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
    var isPerformed: Boolean = false,
): Parcelable {
    fun toWorkout(): com.tomtruyen.data.entities.Workout = com.tomtruyen.data.entities.Workout(
        id = id,
        name = name,
        unit = unit,
        createdAt = createdAt,
        isPerformed = isPerformed
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
                    sets = exercise.sets + com.tomtruyen.data.entities.WorkoutSet(
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
    var exercise: com.tomtruyen.data.entities.Exercise = com.tomtruyen.data.entities.Exercise(),
    var sets: List<com.tomtruyen.data.entities.WorkoutSet> = listOf()
): Parcelable {
    fun toWorkoutExercise(workoutId: String): com.tomtruyen.data.entities.WorkoutExercise =
        com.tomtruyen.data.entities.WorkoutExercise(
            id = id,
            notes = notes,
            rest = rest,
            restEnabled = restEnabled,
            order = order,
            workoutId = workoutId,
            exerciseId = exercise.id
        )
}