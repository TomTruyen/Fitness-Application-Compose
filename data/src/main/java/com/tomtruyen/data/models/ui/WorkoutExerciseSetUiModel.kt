package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.entities.ChangeType
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutExerciseSetUiModel(
    val id: String = UUID.randomUUID().toString(),
    val reps: Int? = null,
    val weight: Double? = null,
    val time: Int? = null,
    val sortOrder: Int = 0,
    val exerciseId: String? = null,
    val completed: Boolean = false,
    val changeRecord: Set<ChangeType> = emptySet(),
)