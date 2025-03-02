package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ExerciseType
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
    val sets: List<WorkoutHistoryExerciseSetUiModel> = emptyList()
) {
    companion object {
        fun fromEntity(entity: WorkoutHistoryExerciseWithSets) = WorkoutHistoryExerciseUiModel(
            id = entity.workoutHistoryExercise.id,
            exerciseId = entity.workoutHistoryExercise.exerciseId,
            name = entity.workoutHistoryExercise.name,
            imageUrl = entity.workoutHistoryExercise.imageUrl,
            type = ExerciseType.fromValue(entity.workoutHistoryExercise.type),
            notes = entity.workoutHistoryExercise.notes,
            sortOrder = entity.workoutHistoryExercise.sortOrder,
            category = entity.workoutHistoryExercise.category,
            equipment = entity.workoutHistoryExercise.equipment,
            sets = entity.sets.map(WorkoutHistoryExerciseSetUiModel::fromEntity).sortedBy { it.sortOrder }
        )
    }
}
