package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository

class ExerciseDetailViewModel(
    private val id: String,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExerciseDetailNavigationType>() {
    val exercise = exerciseRepository.findExerciseById(id)

    private fun delete() = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        exerciseRepository.deleteUserExercise(
            userId = userId,
            exerciseId = id,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    navigate(ExerciseDetailNavigationType.Back)
                }

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    fun onEvent(event: ExerciseDetailUiEvent) {
        when(event) {
            is ExerciseDetailUiEvent.Edit -> navigate(ExerciseDetailNavigationType.Edit(id))
            is ExerciseDetailUiEvent.Delete -> delete()
        }
    }
}
