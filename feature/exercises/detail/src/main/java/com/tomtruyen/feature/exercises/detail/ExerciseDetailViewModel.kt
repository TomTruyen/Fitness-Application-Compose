package com.tomtruyen.feature.exercises.detail

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    private val id: String,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExerciseDetailUiState, ExerciseDetailUiAction, ExerciseDetailUiEvent>(
    initialState = ExerciseDetailUiState()
) {
    init {
        observeLoading()
        observeExercise()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeExercise() = vmScope.launch {
        exerciseRepository.findExerciseById(id)
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { exercise ->
                updateState { it.copy(exercise = exercise) }
            }
    }

    private fun delete() = vmScope.launch {
        val userId = userRepository.getUser()?.id ?: return@launch

        isLoading(true)

        exerciseRepository.deleteUserExercise(
            userId = userId,
            exerciseId = id,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(ExerciseDetailUiEvent.NavigateBack)
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

    override fun onAction(action: ExerciseDetailUiAction) {
        when(action) {
            is ExerciseDetailUiAction.Edit -> triggerEvent(ExerciseDetailUiEvent.NavigateToEdit(id))
            is ExerciseDetailUiAction.Delete -> delete()
        }
    }
}
