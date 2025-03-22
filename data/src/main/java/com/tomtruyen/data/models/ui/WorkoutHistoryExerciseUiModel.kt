package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.BaseExercise
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutHistoryExerciseUiModel(
    override val id: String = UUID.randomUUID().toString(),
    val exerciseId: String? = null,
    val name: String = "",
    override val imageUrl: String? = null,
    override val type: ExerciseType = ExerciseType.WEIGHT,
    override val notes: String? = null,
    val sortOrder: Int = 0,
    val category: String? = null,
    val equipment: String? = null,
    override val sets: List<WorkoutHistoryExerciseSetUiModel> = emptyList(),
    val exercise: ExerciseWithCategoryAndEquipment? = null,
): BaseExercise {
    override val displayName: String
        get() = buildString {
            append(name)

            val equipmentName = equipment.orEmpty()
            if (equipmentName.isNotBlank()) {
                append(" ($equipmentName)")
            }
        }
}
