package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.ExerciseFilter
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val isFromWorkout: Boolean,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExercisesUiState, ExercisesUiAction, ExercisesUiEvent>(
    initialState = ExercisesUiState(isFromWorkout = isFromWorkout)
) {
    val exercises = uiState.flatMapLatest {
        exerciseRepository.findExercises(it.search, it.filter)
    }

    val categories = exerciseRepository.findCategories()
    val equipment = exerciseRepository.findEquipment()

    private val callback by lazy {
        object: FirebaseCallback<List<Exercise>> {
            override fun onError(error: String?) {
                showSnackbar(SnackbarMessage.Error(error))
            }

            override fun onStopLoading() {
                isLoading(false)
            }
        }
    }

    init {
        getExercises()
    }

    private fun getExercises() {
        isLoading(true)
        exerciseRepository.getExercises(callback)

        userRepository.getUser()?.let {
            exerciseRepository.getUserExercises(it.uid, callback)
        }
    }

    override fun onAction(action: ExercisesUiAction) {
        when (action) {
            is ExercisesUiAction.OnToggleSearch -> updateState {
                it.copy(searching = !it.searching)
            }
            is ExercisesUiAction.OnSearchQueryChanged -> updateState {
                it.copy(search = action.query)
            }
            is ExercisesUiAction.OnCategoryFilterChanged -> updateState {
                it.copy(filter = it.filter.copy().apply {
                    tryAddCategory(action.category)
                })
            }
            is ExercisesUiAction.OnEquipmentFilterChanged -> updateState {
                it.copy(filter = it.filter.copy().apply {
                    tryAddEquipment(action.equipment)
                })
            }
            is ExercisesUiAction.OnClearFilterClicked -> updateState {
                it.copy(filter = ExerciseFilter())
            }
            is ExercisesUiAction.OnRemoveFilterClicked -> updateState {
                it.copy(
                    filter = it.filter.copy(
                        categories = it.filter.categories.toMutableList().apply { remove(action.filter) },
                        equipment = it.filter.equipment.toMutableList().apply { remove(action.filter) }
                    )
                )
            }
            is ExercisesUiAction.OnFilterClicked -> triggerEvent(ExercisesUiEvent.NavigateToFilter)
            is ExercisesUiAction.OnAddClicked -> triggerEvent(ExercisesUiEvent.NavigateToAdd)
            is ExercisesUiAction.OnExerciseClicked -> {
                if (isFromWorkout) {
                    updateState {
                        it.copy(
                            selectedExercise = if (it.selectedExercise == action.exercise) null else action.exercise
                        )
                    }
                } else {
                    triggerEvent(ExercisesUiEvent.NavigateToDetail(action.exercise.id))
                }
            }
            is ExercisesUiAction.OnAddExerciseToWorkoutClicked -> {
                triggerEvent(ExercisesUiEvent.NavigateBackToWorkout(action.exercise))
            }
        }
    }

}
