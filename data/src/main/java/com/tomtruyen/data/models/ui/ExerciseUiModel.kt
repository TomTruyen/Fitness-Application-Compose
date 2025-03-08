package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class ExerciseUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val imageUrl: String? = null,
    val imageDetailUrl: String? = null,
    val type: ExerciseType = ExerciseType.WEIGHT,
    val steps: List<String> = emptyList(),
    val category: CategoryUiModel? = null,
    val equipment: EquipmentUiModel? = null,
    val userId: String? = null
) {
    val displayName
        get() = buildString {
            append(name)

            val equipmentName = equipment?.name.orEmpty()
            if (equipmentName.isNotBlank()) {
                append(" ($equipmentName)")
            }
        }

    fun toEntity(userId: String): ExerciseWithCategoryAndEquipment {
        return ExerciseWithCategoryAndEquipment(
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
            category = category?.toEntity(),
            equipment = equipment?.toEntity()
        )
    }

    companion object {
        fun fromEntity(entity: ExerciseWithCategoryAndEquipment) = ExerciseUiModel(
            id = entity.exercise.id,
            name = entity.exercise.name.orEmpty(),
            imageUrl = entity.exercise.imageUrl,
            imageDetailUrl = entity.exercise.imageDetailUrl,
            type = ExerciseType.fromValue(entity.exercise.type),
            steps = entity.exercise.steps.orEmpty(),
            userId = entity.exercise.userId,
            category = entity.category?.let(CategoryUiModel::fromEntity),
            equipment = entity.equipment?.let(EquipmentUiModel::fromEntity)
        )
    }
}