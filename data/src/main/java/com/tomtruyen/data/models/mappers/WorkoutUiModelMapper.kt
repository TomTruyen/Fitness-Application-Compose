package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.models.ui.WorkoutUiModel

object WorkoutUiModelMapper {
    fun toEntity(model: WorkoutUiModel, userId: String? = null): Workout = with(model) {
        Workout(
            id = id,
            name = name,
            unit = unit.value,
            userId = userId ?: this@with.userId,
            sortOrder = sortOrder,
            synced = false,
            duration = duration
        )
    }

    fun toWorkoutHistoryEntity(model: WorkoutUiModel, userId: String): WorkoutHistory =
        with(model) {
            WorkoutHistory(
                name = name,
                unit = unit.value,
                userId = userId,
                duration = duration,
                synced = false,
            )
        }

    fun fromEntity(entity: WorkoutWithExercises): WorkoutUiModel = with(entity) {
        WorkoutUiModel(
            id = workout.id,
            name = workout.name,
            unit = UnitType.fromValue(workout.unit),
            exercises = exercises.map(WorkoutExerciseUiModelMapper::fromEntity)
                .sortedBy { it.sortOrder },
            sortOrder = workout.sortOrder,
            userId = workout.userId,
            duration = workout.duration
        )
    }
}