package com.tomtruyen.feature.workouts.shared

sealed class WorkoutExerciseEvent(
    open val id: String,
) {
    data class OnRepsChanged(
        override val id: String,
        val setIndex: Int,
        val reps: String?
    ) : WorkoutExerciseEvent(id)

    data class OnWeightChanged(
        override val id: String,
        val setIndex: Int,
        val weight: String?
    ) : WorkoutExerciseEvent(id)

    data class OnTimeChanged(
        override val id: String,
        val setIndex: Int,
        val time: Int?
    ) : WorkoutExerciseEvent(id)

    data class OnDeleteSet(
        override val id: String,
        val setIndex: Int
    ) : WorkoutExerciseEvent(id)

    data class OnAddSet(
        override val id: String
    ) : WorkoutExerciseEvent(id)
}