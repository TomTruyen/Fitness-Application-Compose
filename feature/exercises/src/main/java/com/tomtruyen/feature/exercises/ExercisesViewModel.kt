package com.tomtruyen.feature.exercises

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.feature.manager.ExerciseStateManager
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
    private val categoryRepository: CategoryRepository,
    private val equipmentRepository: EquipmentRepository,
    private val userRepository: UserRepository
) : BaseViewModel<ExercisesUiState, ExercisesUiAction, ExercisesUiEvent>(
    initialState = ExercisesUiState(mode = mode)
) {
    private val exerciseStateManager by lazy {
        ExerciseStateManager(
            updateState = ::updateState
        )
    }

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
        categoryRepository.findCategories()
            .distinctUntilChanged()
            .collectLatest { categories ->
                updateState { it.copy(categories = categories) }
            }
    }

    private fun observeEquipment() = vmScope.launch {
        equipmentRepository.findEquipment()
            .distinctUntilChanged()
            .collectLatest { equipment ->
                updateState { it.copy(equipment = equipment) }
            }
    }

    private fun handleExerciseClick(exercise: ExerciseUiModel) {
        when (mode) {
            Screen.Exercise.Overview.Mode.VIEW -> {
                triggerEvent(ExercisesUiEvent.Navigate.Exercise.Detail(exercise.id))
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
            ExercisesUiAction.OnRefresh -> fetchExercises(true)

            ExercisesUiAction.OnFilterClicked -> triggerEvent(
                ExercisesUiEvent.Navigate.Exercise.Filter
            )

            ExercisesUiAction.OnAddClicked -> triggerEvent(
                ExercisesUiEvent.Navigate.Exercise.Add
            )

            is ExercisesUiAction.OnDetailClicked -> handleExerciseClick(
                exercise = action.exercise
            )

            ExercisesUiAction.Workout.AddExercise -> triggerEvent(
                ExercisesUiEvent.Navigate.Workout.Back(uiState.value.selectedExercises)
            )

            else -> exerciseStateManager.onAction(action)
        }
    }

}
