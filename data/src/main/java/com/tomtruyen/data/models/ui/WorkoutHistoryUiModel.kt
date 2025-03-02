package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutHistoryUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val unit: UnitType = UnitType.KG,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val exercises: List<WorkoutHistoryExerciseUiModel> = emptyList()
) {
    companion object {
        fun fromEntity(entity: WorkoutHistoryWithExercises) = WorkoutHistoryUiModel(
            id = entity.workoutHistory.id,
            name = entity.workoutHistory.name,
            unit = UnitType.fromValue(entity.workoutHistory.unit),
            createdAt = entity.workoutHistory.createdAt,
            exercises = entity.exercises.map(WorkoutHistoryExerciseUiModel::fromEntity).sortedBy { it.sortOrder }
        )
    }
}