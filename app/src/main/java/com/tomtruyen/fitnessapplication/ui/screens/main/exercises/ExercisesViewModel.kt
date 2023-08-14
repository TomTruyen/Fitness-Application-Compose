package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val exerciseRepository: ExerciseRepository
): BaseViewModel<ExercisesNavigationType>() {
    val state = MutableStateFlow(ExercisesUiState())

    val exercises = state.flatMapLatest {
        exerciseRepository.findExercises(it.search)
    }

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
            is ExercisesUiEvent.OnFilterClicked -> navigate(ExercisesNavigationType.Filter)
            is ExercisesUiEvent.OnToggleSearch -> state.value = state.value.copy(
                searching = !state.value.searching
            )
            is ExercisesUiEvent.OnAddClicked -> navigate(ExercisesNavigationType.Add)
            is ExercisesUiEvent.OnSearchQueryChanged -> state.value = state.value.copy(
                search = event.query
            )
        }
    }
}
