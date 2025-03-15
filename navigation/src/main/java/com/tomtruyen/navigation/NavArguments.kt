package com.tomtruyen.navigation

import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import kotlinx.serialization.Serializable

@Serializable
sealed class NavResult {
    abstract val key: String

    @Serializable
    data class ExerciseResult(
        val mode: ExerciseMode,
        val exercises: List<ExerciseUiModel>
    ): NavResult() {
        override val key: String
            get() = KEY

        companion object {
            const val KEY = "exercises"
        }
    }

    @Serializable
    data class ReorderExerciseResult(
        val exercises: List<WorkoutExerciseUiModel>
    ): NavResult() {
        override val key: String
            get() = KEY

        companion object {
            const val KEY = "workout_exercises"
        }
    }
}