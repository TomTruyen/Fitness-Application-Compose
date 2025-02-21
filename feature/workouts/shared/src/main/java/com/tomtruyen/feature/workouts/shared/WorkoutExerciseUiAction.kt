package com.tomtruyen.feature.workouts.shared

sealed class WorkoutExerciseUiAction(
    open val id: String,
) {
    data class OnRepsChanged(
        override val id: String,
        val setIndex: Int,
        val reps: String?
    ) : WorkoutExerciseUiAction(id)

    data class OnWeightChanged(
        override val id: String,
        val setIndex: Int,
        val weight: String?
    ) : WorkoutExerciseUiAction(id)

    data class OnTimeChanged(
        override val id: String,
        val setIndex: Int,
        val time: Int?
    ) : WorkoutExerciseUiAction(id)

    data class OnDeleteSet(
        override val id: String,
        val setIndex: Int
    ) : WorkoutExerciseUiAction(id)

    data class OnAddSet(
        override val id: String
    ) : WorkoutExerciseUiAction(id)
}