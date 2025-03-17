package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutExerciseUiModel(
    val id: String = UUID.randomUUID().toString(),
    val exerciseId: String = "",
    val name: String = "",
    val imageUrl: String? = null,
    val imageDetailUrl: String? = null,
    val type: ExerciseType = ExerciseType.WEIGHT,
    val steps: List<String> = emptyList(),
    val notes: String? = null,
    val sortOrder: Int = 0,
    val category: CategoryUiModel? = null,
    val equipment: EquipmentUiModel? = null,
    val sets: List<WorkoutExerciseSetUiModel> = emptyList()
) {
    val displayName
        get() = buildString {
            append(name)

            val equipmentName = equipment?.name.orEmpty()
            if (equipmentName.isNotBlank()) {
                append(" ($equipmentName)")
            }
        }

    // Only checking
    fun isOriginalExercise(other: WorkoutExerciseUiModel): Boolean {
        return this.sortOrder == other.sortOrder &&
                this.sets.size == other.sets.size
    }

    companion object {
        fun createFromExerciseModel(model: ExerciseUiModel, setCount: Int) = WorkoutExerciseUiModel(
            exerciseId = model.id,
            name = model.name,
            imageUrl = model.imageUrl,
            imageDetailUrl = model.imageDetailUrl,
            type = model.type,
            steps = model.steps,
            notes = null,
            sortOrder = 0,
            category = model.category,
            equipment = model.equipment,
            sets = List(setCount) { WorkoutExerciseSetUiModel() }
        )
    }
}