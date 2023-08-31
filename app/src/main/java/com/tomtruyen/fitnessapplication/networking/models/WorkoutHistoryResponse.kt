package com.tomtruyen.fitnessapplication.networking.models

import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistory
import java.util.UUID

data class WorkoutHistoriesResponse(
    val data: List<WorkoutHistoryResponse> = emptyList()
)

data class WorkoutHistoryResponse(
    var id: String = UUID.randomUUID().toString(),
    var duration: Long,
    var createdAt: Long = System.currentTimeMillis(),
    var workout: WorkoutResponse,
) {
    fun toWorkoutHistory() = WorkoutHistory(
        id = id,
        duration = duration,
        createdAt = createdAt,
        workoutId = workout.id
    )
}