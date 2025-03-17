package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
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
}