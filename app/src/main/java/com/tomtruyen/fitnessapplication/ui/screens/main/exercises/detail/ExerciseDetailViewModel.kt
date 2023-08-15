package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository

class ExerciseDetailViewModel(
    id: String,
    exerciseRepository: ExerciseRepository,
): BaseViewModel<ExerciseDetailNavigationType>() {
    val exercise = exerciseRepository.findExerciseById(id)
}
