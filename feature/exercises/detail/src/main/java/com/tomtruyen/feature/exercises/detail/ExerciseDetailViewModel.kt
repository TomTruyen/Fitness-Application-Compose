package com.tomtruyen.feature.exercises.detail

import com.tomtruyen.core.common.base.BaseViewModel
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
) : BaseViewModel<ExerciseDetailUiState, ExerciseDetailUiAction, ExerciseDetailUiEvent>(
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
        exerciseRepository.findExerciseByIdAsync(id)
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { exercise ->
                updateState { it.copy(exercise = exercise) }
            }
    }

    private fun delete() = launchLoading {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        exerciseRepository.deleteExercise(
            userId = userId,
            exerciseId = id,
        )

        triggerEvent(ExerciseDetailUiEvent.NavigateBack)
    }

    private fun showSheet(show: Boolean) = updateState {
        it.copy(
            showSheet = show
        )
    }

    override fun onAction(action: ExerciseDetailUiAction) {
        when (action) {
            is ExerciseDetailUiAction.Edit -> triggerEvent(
                ExerciseDetailUiEvent.NavigateToEdit(id)
            )

            is ExerciseDetailUiAction.Delete -> delete()

            ExerciseDetailUiAction.Sheet.Show -> showSheet(true)

            ExerciseDetailUiAction.Sheet.Dismiss -> showSheet(false)
        }
    }
}
