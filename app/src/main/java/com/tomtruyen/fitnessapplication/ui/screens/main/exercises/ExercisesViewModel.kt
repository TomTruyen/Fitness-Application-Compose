package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

class ExercisesViewModel(
    private val exerciseRepository: ExerciseRepository
): BaseViewModel<ExercisesNavigationType>() {
    val state = MutableStateFlow(ExercisesUiState())

    val exercises = exerciseRepository.findExercises()

    init {
        getExercises()
    }

    private fun getExercises() = launchLoading {
        exerciseRepository.getExercises(object: FirebaseCallback<List<Exercise>> {
            override fun onSuccess(value: List<Exercise>) {}

            override fun onError(error: String?) {
                showSnackbar(SnackbarMessage.Error(error))
            }
        })
    }

    fun onEvent(event: ExercisesUiEvent) {
        when(event) {
            else -> Unit
        }
    }
}
