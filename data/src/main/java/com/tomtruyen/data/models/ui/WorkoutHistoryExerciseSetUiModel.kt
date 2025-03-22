package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.ChangeType
import com.tomtruyen.core.common.models.ExerciseSet
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class WorkoutHistoryExerciseSetUiModel(
    override val id: String = UUID.randomUUID().toString(),
    override val reps: Int? = null,
    override val weight: Double? = null,
    override val time: Int? = null,
    override val sortOrder: Int = 0,
    override val completed: Boolean = true,
    override val changeRecord: Set<ChangeType> = emptySet(),
): ExerciseSet
