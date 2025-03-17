package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutHistoryExerciseSetUiModel(
    val id: String = UUID.randomUUID().toString(),
    val reps: Int? = null,
    val weight: Double? = null,
    val time: Int? = null,
    val sortOrder: Int = 0
)
