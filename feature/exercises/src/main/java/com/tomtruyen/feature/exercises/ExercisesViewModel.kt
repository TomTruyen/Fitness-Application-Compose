package com.tomtruyen.feature.exercises

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.models.ExerciseFilter
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModel(
    private val mode: Screen.Exercise.Overview.Mode,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExercisesUiState, ExercisesUiAction, ExercisesUiEvent>(
    initialState = ExercisesUiState(mode = mode)
) {
    init {
        fetchExercises()

        observeLoading()
        observeRefreshing()
        observeExercises()
        observeCategories()
        observeEquipment()
    }

    private fun fetchExercises(refresh: Boolean = false) = launchLoading(refresh) {
        exerciseRepository.getExercises(userRepository.getUser()?.id, refresh)
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeRefreshing() = vmScope.launch {
        refreshing.collectLatest { refreshing ->
            updateState { it.copy(refreshing = refreshing) }
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

    private fun handleExerciseClick(exercise: Exercise) {
        when(mode) {
            Screen.Exercise.Overview.Mode.VIEW -> {
                triggerEvent(ExercisesUiEvent.NavigateToDetail(exercise.id))
            }
            Screen.Exercise.Overview.Mode.SELECT -> {
                updateState {
                    it.copy(
                        selectedExercises = it.selectedExercises.toMutableList().apply {
                            if (contains(exercise)) {
                                remove(exercise)
                            } else {
                                add(exercise)
                            }
                        },
                    )
                }
            }
            Screen.Exercise.Overview.Mode.REPLACE -> {
                updateState {
                    it.copy(
                        selectedExercises = listOf(exercise)
                    )
                }
            }
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
            is ExercisesUiAction.OnExerciseClicked -> handleExerciseClick(action.exercise)
            is ExercisesUiAction.OnAddExerciseToWorkoutClicked -> {
                triggerEvent(ExercisesUiEvent.NavigateBackToWorkout(uiState.value.selectedExercises))
            }

            is ExercisesUiAction.OnRefresh -> fetchExercises(
                refresh = true
            )
        }
    }

}
