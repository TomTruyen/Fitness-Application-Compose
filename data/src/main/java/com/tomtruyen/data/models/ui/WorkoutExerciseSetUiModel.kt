package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.WorkoutExerciseSet
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutExerciseSetUiModel(
    val id: String = UUID.randomUUID().toString(),
    val reps: Int? = null,
    val weight: Double? = null,
    val time: Int? = null,
    val sortOrder: Int = 0,
    val completed: Boolean = false
) {
    fun toEntity(workoutExerciseId: String) = WorkoutExerciseSet(
        id = id,
        reps = reps,
        weight = weight,
        time = time,
        sortOrder = sortOrder,
        workoutExerciseId = workoutExerciseId
    )

    companion object {
        fun fromEntity(entity: WorkoutExerciseSet) = WorkoutExerciseSetUiModel(
            id = entity.id,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            sortOrder = entity.sortOrder
        )
    }
}