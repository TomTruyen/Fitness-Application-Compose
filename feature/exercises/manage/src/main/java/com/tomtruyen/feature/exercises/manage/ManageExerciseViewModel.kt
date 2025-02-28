package com.tomtruyen.feature.exercises.manage

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.feature.exercises.manage.model.ManageExerciseMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ManageExerciseViewModel(
    private val id: String?,
    private val exerciseRepository: ExerciseRepository,
    private val categoryRepository: CategoryRepository,
    private val equipmentRepository: EquipmentRepository,
    private val userRepository: UserRepository
) : BaseViewModel<ManageExerciseUiState, ManageExerciseUiAction, ManageExerciseUiEvent>(
    initialState = ManageExerciseUiState(
        mode = ManageExerciseMode.fromArgs(id)
    )
) {
    init {
        findExercise()

        observeLoading()
        observeCategories()
        observeEquipment()
    }

    private fun findExercise() = launchLoading {
        if (uiState.value.mode == ManageExerciseMode.CREATE || id == null) return@launchLoading

        exerciseRepository.findExerciseById(id)?.let {
            updateAndGetState { state ->
                state.copy(
                    initialExercise = it,
                    fullExercise = it
                )
            }.also { state ->
                state.validateAll()
            }
        }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeCategories() = vmScope.launch {
        categoryRepository.findCategories()
            .distinctUntilChanged()
            .collectLatest { categories ->
                updateState {
                    it.copy(categories = listOf(Category.DEFAULT) + categories)
                }
            }
    }

    private fun observeEquipment() = vmScope.launch {
        equipmentRepository.findEquipment()
            .distinctUntilChanged()
            .collectLatest { equipment ->
                updateState {
                    it.copy(
                        equipment = listOf(Equipment.DEFAULT) + equipment
                    )
                }
            }
    }

    private fun save() = launchLoading {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        exerciseRepository.saveExercise(
            userId = userId,
            userExercise = uiState.value.fullExercise,
        )

        triggerEvent(ManageExerciseUiEvent.NavigateBack)
    }

    override fun onAction(action: ManageExerciseUiAction) {
        when (action) {
            is ManageExerciseUiAction.OnExerciseNameChanged -> updateState {
                it.copy(
                    fullExercise = it.fullExercise.copy(
                        exercise = it.fullExercise.exercise.copy(
                            name = action.name
                        )
                    ),
                    nameValidationResult = it.validateName(action.name)
                )
            }

            is ManageExerciseUiAction.OnCategoryChanged -> updateState {
                it.copy(
                    fullExercise = it.fullExercise.copy(
                        category = action.category as Category
                    ),
                )
            }

            is ManageExerciseUiAction.OnEquipmentChanged -> updateState {
                it.copy(
                    fullExercise = it.fullExercise.copy(
                        equipment = action.equipment as Equipment
                    ),
                )
            }

            is ManageExerciseUiAction.OnTypeChanged -> updateState {
                it.copy(
                    fullExercise = it.fullExercise.copy(
                        exercise = it.fullExercise.exercise.copy(
                            type = action.type
                        ),
                    ),
                )
            }

            is ManageExerciseUiAction.OnSaveClicked -> save()
        }
    }
}
