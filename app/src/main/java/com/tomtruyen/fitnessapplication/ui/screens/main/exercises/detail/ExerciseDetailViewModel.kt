package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository

class ExerciseDetailViewModel(
    private val id: String,
    exerciseRepository: ExerciseRepository,
): BaseViewModel<ExerciseDetailNavigationType>() {
    val exercise = exerciseRepository.findExerciseById(id)

    private fun delete() {
        // TODO: Delete Exercise from db

        navigate(ExerciseDetailNavigationType.Back)
    }

    fun onEvent(event: ExerciseDetailUiEvent) {
        when(event) {
            is ExerciseDetailUiEvent.Edit -> navigate(ExerciseDetailNavigationType.Edit(id))
            is ExerciseDetailUiEvent.Delete -> delete()
        }
    }
}
