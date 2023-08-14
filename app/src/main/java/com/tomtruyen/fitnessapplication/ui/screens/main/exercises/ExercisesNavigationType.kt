package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

sealed class ExercisesNavigationType {
    data object Filter : ExercisesNavigationType()
    data object Add: ExercisesNavigationType()
}
