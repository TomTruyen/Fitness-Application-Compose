package com.tomtruyen.feature.exercises

import com.tomtruyen.data.models.ui.ExerciseUiModel

sealed class ExercisesUiEvent {
    sealed class Navigate: ExercisesUiEvent() {
        sealed class Exercise: Navigate() {
            data object Filter: Exercise()

            data object Add: Exercise()

            data class Detail(val id: String): Exercise()
        }

        sealed class Workout: Navigate() {
            data class Back(val exercises: List<ExerciseUiModel>): Workout()
        }

        data object Back: Navigate()
    }
}
