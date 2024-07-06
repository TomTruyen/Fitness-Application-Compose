package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.models.ExerciseFilter
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val isFromWorkout: Boolean,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExercisesUiState, ExercisesUiAction, ExercisesUiEvent>(
    initialState = ExercisesUiState(isFromWorkout = isFromWorkout)
) {
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
        fetchExercises()

        observeLoading()
        observeExercises()
        observeCategories()
        observeEquipment()
    }

    private fun fetchExercises() {
        isLoading(true)
        exerciseRepository.getExercises(callback)

        userRepository.getUser()?.let {
            exerciseRepository.getUserExercises(it.uid, callback)
        }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeExercises() = vmScope.launch {
        uiState.distinctUntilChanged { old, new ->
            old.search == new.search && old.filter == new.filter
        }.flatMapLatest {
            exerciseRepository.findExercises(it.search, it.filter)
        }.distinctUntilChanged().collectLatest { exercises ->
            updateState { it.copy(exercises = exercises) }
        }
    }

    private fun observeCategories() = vmScope.launch {
        exerciseRepository.findCategories()
            .distinctUntilChanged()
            .collectLatest { categories ->
                updateState { it.copy(categories = categories) }
            }
    }

    private fun observeEquipment() = vmScope.launch {
        exerciseRepository.findEquipment()
            .distinctUntilChanged()
            .collectLatest { equipment ->
                updateState { it.copy(equipment = equipment) }
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
                it.copy(filter = com.tomtruyen.models.ExerciseFilter())
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
