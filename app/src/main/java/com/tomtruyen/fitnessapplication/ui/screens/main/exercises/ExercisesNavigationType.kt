package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.data.entities.Exercise

sealed class ExercisesNavigationType {
    data object Filter : ExercisesNavigationType()
    data object Add: ExercisesNavigationType()
    data object Back: ExercisesNavigationType()
    data class Detail(val id: String): ExercisesNavigationType()
    data class BackToWorkout(val exercise: Exercise): ExercisesNavigationType()
}
