package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.WorkoutHistoryExerciseWithSets
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel

object WorkoutHistoryExerciseUiModelMapper {
    fun fromEntity(
        entity: WorkoutHistoryExerciseWithSets
    ): WorkoutHistoryExerciseUiModel = with(entity) {
        WorkoutHistoryExerciseUiModel(
            id = workoutHistoryExercise.id,
            exerciseId = workoutHistoryExercise.exerciseId,
            name = workoutHistoryExercise.name,
            imageUrl = workoutHistoryExercise.imageUrl,
            type = ExerciseType.fromValue(workoutHistoryExercise.type),
            notes = workoutHistoryExercise.notes,
            sortOrder = workoutHistoryExercise.sortOrder,
            category = workoutHistoryExercise.category,
            equipment = workoutHistoryExercise.equipment,
            sets = sets.map(WorkoutHistoryExerciseSetUiModelMapper::fromEntity)
                .sortedBy { it.sortOrder },
            exercise = exercise
        )
    }
}