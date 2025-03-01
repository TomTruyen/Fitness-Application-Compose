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
    // TODO: When converting back to the Entity ensure that we set workoutId but we can get that from parent workout

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