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
    val duration: Long = 0L,
    val createdAt: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val page: Int,
    val exercises: List<WorkoutHistoryExerciseUiModel> = emptyList()
) {
    companion object {
        fun fromEntity(entity: WorkoutHistoryWithExercises) = WorkoutHistoryUiModel(
            id = entity.workoutHistory.id,
            name = entity.workoutHistory.name,
            unit = UnitType.fromValue(entity.workoutHistory.unit),
            duration = entity.workoutHistory.duration,
            createdAt = entity.workoutHistory.createdAt,
            page = entity.workoutHistory.page,
            exercises = entity.exercises.map(WorkoutHistoryExerciseUiModel::fromEntity)
                .sortedBy { it.sortOrder }
        )
    }
}