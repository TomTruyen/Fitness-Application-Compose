package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseWithSets
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.text.category

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

    fun toEntity(workoutId: String, index: Int) = WorkoutExercise(
        id = id,
        exerciseId = exerciseId,
        workoutId = workoutId,
        notes = notes,
        sortOrder = index,
        synced = false
    )

    fun toWorkoutHistoryExerciseEntity(workoutHistoryId: String, index: Int) =
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

        fun fromEntity(entity: WorkoutExerciseWithSets) = WorkoutExerciseUiModel(
            id = entity.workoutExercise.id,
            exerciseId = entity.workoutExercise.exerciseId,
            name = entity.exercise.exercise.name,
            imageUrl = entity.exercise.exercise.imageUrl,
            imageDetailUrl = entity.exercise.exercise.imageDetailUrl,
            type = ExerciseType.fromValue(entity.exercise.exercise.type),
            steps = entity.exercise.exercise.steps.orEmpty(),
            notes = entity.workoutExercise.notes,
            sortOrder = entity.workoutExercise.sortOrder,
            category = entity.exercise.category?.let(CategoryUiModel::fromEntity),
            equipment = entity.exercise.equipment?.let(EquipmentUiModel::fromEntity),
            sets = entity.sets.map(WorkoutExerciseSetUiModel::fromEntity).sortedBy { it.sortOrder }
        )

        fun fromHistory(
            entity: WorkoutHistoryExerciseUiModel,
            exercise: ExerciseWithCategoryAndEquipment
        ) = WorkoutExerciseUiModel(
            exerciseId = exercise.exercise.id,
            name = exercise.exercise.name,
            imageUrl = exercise.exercise.imageUrl,
            imageDetailUrl = exercise.exercise.imageDetailUrl,
            type = ExerciseType.fromValue(exercise.exercise.type),
            steps = exercise.exercise.steps.orEmpty(),
            notes = entity.notes,
            sortOrder = entity.sortOrder,
            category = exercise.category?.let(CategoryUiModel::fromEntity),
            equipment = exercise.equipment?.let(EquipmentUiModel::fromEntity),
            sets = entity.sets.map(WorkoutExerciseSetUiModel::fromHistory).sortedBy { it.sortOrder }
        )
    }

    // Only checking
    fun isOriginalExercise(other: WorkoutExerciseUiModel): Boolean {
        return this.sortOrder == other.sortOrder &&
                this.sets.size == other.sets.size
    }
}