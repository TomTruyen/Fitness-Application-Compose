package com.tomtruyen.data.models.mappers

import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseSetUiModel

object WorkoutHistoryExerciseSetUiModelMapper {
    fun fromEntity(entity: WorkoutHistoryExerciseSet): WorkoutHistoryExerciseSetUiModel = with(entity) {
        WorkoutHistoryExerciseSetUiModel(
            id = id,
            reps = reps,
            weight = weight,
            time = time,
            sortOrder = sortOrder
        )
    }
}