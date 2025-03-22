package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.BaseExercise
import com.tomtruyen.core.common.models.ExerciseType
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutExerciseUiModel(
    override val id: String = UUID.randomUUID().toString(),
    val exerciseId: String = "",
    val name: String = "",
    override val imageUrl: String? = null,
    val imageDetailUrl: String? = null,
    override val type: ExerciseType = ExerciseType.WEIGHT,
    val steps: List<String> = emptyList(),
    override val notes: String? = null,
    val sortOrder: Int = 0,
    val category: CategoryUiModel? = null,
    val equipment: EquipmentUiModel? = null,
    override val sets: List<WorkoutExerciseSetUiModel> = emptyList()
): BaseExercise {
    override val displayName
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