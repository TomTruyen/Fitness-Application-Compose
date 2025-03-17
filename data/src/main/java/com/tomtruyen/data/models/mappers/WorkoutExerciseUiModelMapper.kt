package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseWithSets
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutHistoryExerciseUiModel
import kotlin.collections.orEmpty

object WorkoutExerciseUiModelMapper {
    fun toEntity(
        model: WorkoutExerciseUiModel,
        workoutId: String,
        index: Int
    ): WorkoutExercise = with(model) {
        WorkoutExercise(
            id = id,
            exerciseId = exerciseId,
            workoutId = workoutId,
            notes = notes,
            sortOrder = index,
            synced = false
        )
    }

    fun toWorkoutHistoryExerciseEntity(
        model: WorkoutExerciseUiModel,
        workoutHistoryId: String,
        index: Int
    ): WorkoutHistoryExercise = with(model) {
        WorkoutHistoryExercise(
            name = name,
            imageUrl = imageUrl,
            type = type.value,
            notes = notes,
            sortOrder = index,
            exerciseId = exerciseId,
            workoutHistoryId = workoutHistoryId,
            category = category?.name,
            equipment = equipment?.name,
            synced = false
        )
    }

    fun fromEntity(
        entity: WorkoutExerciseWithSets
    ): WorkoutExerciseUiModel = with(entity) {
        WorkoutExerciseUiModel(
            id = workoutExercise.id,
            exerciseId = workoutExercise.exerciseId,
            name = exercise.exercise.name,
            imageUrl = exercise.exercise.imageUrl,
            imageDetailUrl = exercise.exercise.imageDetailUrl,
            type = ExerciseType.fromValue(exercise.exercise.type),
            steps = exercise.exercise.steps.orEmpty(),
            notes = workoutExercise.notes,
            sortOrder = workoutExercise.sortOrder,
            category = exercise.category?.let(CategoryUiModelMapper::fromEntity),
            equipment = exercise.equipment?.let(EquipmentUiModelMapper::fromEntity),
            sets = sets.map(WorkoutExerciseSetUiModelMapper::fromEntity).sortedBy { it.sortOrder }
        )
    }

    fun fromWorkoutHistoryExerciseEntity(
        entity: WorkoutHistoryExerciseUiModel,
        exercise: ExerciseWithCategoryAndEquipment
    ): WorkoutExerciseUiModel = with(entity) {
        WorkoutExerciseUiModel(
            exerciseId = exercise.exercise.id,
            name = exercise.exercise.name,
            imageUrl = exercise.exercise.imageUrl,
            imageDetailUrl = exercise.exercise.imageDetailUrl,
            type = ExerciseType.fromValue(exercise.exercise.type),
            steps = exercise.exercise.steps.orEmpty(),
            notes = notes,
            sortOrder = sortOrder,
            category = exercise.category?.let(CategoryUiModelMapper::fromEntity),
            equipment = exercise.equipment?.let(EquipmentUiModelMapper::fromEntity),
            sets = sets.map(WorkoutExerciseSetUiModelMapper::fromWorkoutHistorySetEntity).sortedBy { it.sortOrder }
        )
    }
}