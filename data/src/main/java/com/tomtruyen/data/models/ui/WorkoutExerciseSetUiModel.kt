package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val exerciseId: String? = null,
    val completed: Boolean = false
) {
    fun toEntity(workoutExerciseId: String, index: Int) = WorkoutExerciseSet(
        id = id,
        reps = reps,
        weight = weight,
        time = time,
        completed = completed,
        sortOrder = index,
        workoutExerciseId = workoutExerciseId,
        synced = false
    )

    fun toWorkoutHistorySetEntity(
        workoutHistoryExerciseId: String,
        index: Int
    ) = WorkoutHistoryExerciseSet(
        reps = reps,
        weight = weight,
        time = time,
        sortOrder = index,
        workoutHistoryExerciseId = workoutHistoryExerciseId,
        synced = false
    )

    companion object {
        fun fromEntity(entity: WorkoutExerciseSet) = WorkoutExerciseSetUiModel(
            id = entity.id,
            reps = entity.reps,
            weight = entity.weight,
            time = entity.time,
            completed = entity.completed,
            sortOrder = entity.sortOrder
        )
    }
}