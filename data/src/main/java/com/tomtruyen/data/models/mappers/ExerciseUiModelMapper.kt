package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.ExerciseUiModel

object ExerciseUiModelMapper {
    fun toEntity(model: ExerciseUiModel, userId: String): ExerciseWithCategoryAndEquipment =
        with(model) {
            ExerciseWithCategoryAndEquipment(
                exercise = Exercise(
                    id = id,
                    name = name,
                    imageUrl = imageUrl,
                    imageDetailUrl = imageDetailUrl,
                    type = type.value,
                    steps = steps,
                    equipmentId = if (equipment != EquipmentUiModel.DEFAULT) {
                        equipment?.id
                    } else null,
                    categoryId = if (category != CategoryUiModel.DEFAULT) {
                        category?.id
                    } else null,
                    userId = userId,
                    synced = false
                ),
                category = category?.let(CategoryUiModelMapper::toEntity),
                equipment = equipment?.let(EquipmentUiModelMapper::toEntity)
            )
        }

    fun fromEntity(entity: ExerciseWithCategoryAndEquipment): ExerciseUiModel = with(entity) {
        ExerciseUiModel(
            id = exercise.id,
            name = exercise.name,
            imageUrl = exercise.imageUrl,
            imageDetailUrl = exercise.imageDetailUrl,
            type = ExerciseType.fromValue(exercise.type),
            steps = exercise.steps.orEmpty(),
            userId = exercise.userId,
            category = category?.let(CategoryUiModelMapper::fromEntity),
            equipment = equipment?.let(EquipmentUiModelMapper::fromEntity)
        )
    }
}