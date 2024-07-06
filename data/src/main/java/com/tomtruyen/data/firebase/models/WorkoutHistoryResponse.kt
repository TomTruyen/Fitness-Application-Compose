package com.tomtruyen.data.firebase.models

import com.tomtruyen.data.entities.WorkoutHistory
import java.util.UUID

data class WorkoutHistoriesResponse(
    val data: List<WorkoutHistoryResponse> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val UPDATED_AT_ORDER_FIELD = "updatedAt"
    }
}

data class WorkoutHistoryResponse(
    var id: String = UUID.randomUUID().toString(),
    var duration: Long = 0,
    var createdAt: Long = System.currentTimeMillis(),
    var workout: WorkoutResponse = WorkoutResponse(),
) {
    fun toWorkoutHistory() = com.tomtruyen.data.entities.WorkoutHistory(
        id = id,
        duration = duration,
        createdAt = createdAt,
        workoutId = workout.id
    )
}