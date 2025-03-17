package com.tomtruyen.data.models.mappers

import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseSetUiModel

object WorkoutExerciseSetUiModelMapper {
    fun toEntity(
        model: WorkoutExerciseSetUiModel,
        workoutExerciseId: String,
        index: Int,
        withChangeRecord: Boolean = false
    ): WorkoutExerciseSet = with(model) {
        WorkoutExerciseSet(
            id = id,
            reps = reps,
            weight = weight,
            time = time,
            completed = completed,
            sortOrder = index,
            workoutExerciseId = workoutExerciseId,
            synced = false,
            changeRecord = if (withChangeRecord) changeRecord else emptySet()
        )
    }

    fun toWorkoutHistorySetEntity(
        model: WorkoutExerciseSetUiModel,
        workoutHistoryExerciseId: String,
        index: Int
    ): WorkoutHistoryExerciseSet = with(model) {
        WorkoutHistoryExerciseSet(
            reps = reps,
            weight = weight,
            time = time,
            sortOrder = index,
            workoutHistoryExerciseId = workoutHistoryExerciseId,
            synced = false
        )
    }

    fun fromEntity(entity: WorkoutExerciseSet): WorkoutExerciseSetUiModel = with(entity) {
        WorkoutExerciseSetUiModel(
            id = id,
            reps = reps,
            weight = weight,
            time = time,
            completed = completed,
            sortOrder = sortOrder,
            changeRecord = changeRecord
        )
    }

    fun fromWorkoutHistorySetEntity(
        entity: WorkoutHistoryExerciseSetUiModel
    ): WorkoutExerciseSetUiModel = with(entity) {
        WorkoutExerciseSetUiModel(
            id = id,
            reps = reps,
            weight = weight,
            time = time,
            completed = false,
            sortOrder = sortOrder,
            changeRecord = emptySet()
        )
    }
}