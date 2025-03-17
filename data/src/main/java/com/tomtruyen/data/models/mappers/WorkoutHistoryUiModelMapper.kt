package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel

object WorkoutHistoryUiModelMapper {
    fun toWorkoutUiModel(model: WorkoutHistoryUiModel): WorkoutUiModel = with(model) {
        WorkoutUiModel(
            name = name,
            unit = unit,
            exercises = exercises.filter {
                it.exercise != null
            }.map {
                WorkoutExerciseUiModelMapper.fromWorkoutHistoryExerciseEntity(
                    entity = it,
                    exercise = it.exercise!!
                )
            }
        )
    }

    fun fromEntity(entity: WorkoutHistoryWithExercises): WorkoutHistoryUiModel = with(entity) {
        WorkoutHistoryUiModel(
            id = workoutHistory.id,
            name = workoutHistory.name,
            unit = UnitType.fromValue(workoutHistory.unit),
            duration = workoutHistory.duration,
            createdAt = workoutHistory.createdAt,
            page = workoutHistory.page,
            exercises = exercises.map(WorkoutHistoryExerciseUiModelMapper::fromEntity).sortedBy { it.sortOrder }
        )
    }
}