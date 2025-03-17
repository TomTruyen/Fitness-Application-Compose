package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.entities.WorkoutHistoryExerciseWithSets
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutHistoryExerciseUiModel(
    val id: String = UUID.randomUUID().toString(),
    val exerciseId: String? = null,
    val name: String = "",
    val imageUrl: String? = null,
    val type: ExerciseType = ExerciseType.WEIGHT,
    val notes: String? = null,
    val sortOrder: Int = 0,
    val category: String? = null,
    val equipment: String? = null,
    val sets: List<WorkoutHistoryExerciseSetUiModel> = emptyList(),
    val exercise: ExerciseWithCategoryAndEquipment? = null,
) {
    val displayName: String
        get() = buildString {
            append(name)

            val equipmentName = equipment.orEmpty()
            if (equipmentName.isNotBlank()) {
                append(" ($equipmentName)")
            }
        }
}
