package com.tomtruyen.feature.exercises.manage

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.feature.exercises.manage.model.ManageExerciseMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ManageExerciseViewModel(
    private val id: String?,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ManageExerciseUiState, ManageExerciseUiAction, ManageExerciseUiEvent>(
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
        if(uiState.value.mode == ManageExerciseMode.CREATE || id == null) return@launchLoading

        // TODO: Replace with findExerciseById
//        exerciseRepository.findUserExerciseById(id)?.let {
//            updateAndGetState { state ->
//                state.copy(
//                    initialExercise = it,
//                    exercise = it
//                )
//            }.also { state ->
//                state.validateAll()
//            }
//        }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeCategories() = vmScope.launch {
        // TODO: Implement
//        exerciseRepository.findCategories()
//            .distinctUntilChanged()
//            .collectLatest { categories ->
//                updateState {
//                    it.copy(categories = listOf(Exercise.DEFAULT_DROPDOWN_VALUE) + categories)
//                }
//            }
    }

    private fun observeEquipment() = vmScope.launch {
        // TODO: Implement
//        exerciseRepository.findEquipment()
//            .distinctUntilChanged()
//            .collectLatest { equipment ->
//                updateState {
//                    it.copy(
//                        equipment = listOf(Exercise.DEFAULT_DROPDOWN_VALUE) + equipment
//                    )
//                }
//            }
    }

    private fun save() = launchLoading {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        exerciseRepository.saveUserExercise(
            userId = userId,
            exercise = uiState.value.exercise,
        )

        triggerEvent(ManageExerciseUiEvent.NavigateBack)
    }

    override fun onAction(action: ManageExerciseUiAction) {
        when(action) {
            is ManageExerciseUiAction.OnExerciseNameChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        name = action.name,
                    ),
                    nameValidationResult = it.validateName(action.name)
                )
            }
            is ManageExerciseUiAction.OnCategoryChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        category = action.category
                    ),
                )
            }
            is ManageExerciseUiAction.OnEquipmentChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        equipment = action.equipment
                    )
                )
            }
            is ManageExerciseUiAction.OnTypeChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        type = action.type
                    ),
                )
            }
            is ManageExerciseUiAction.OnSaveClicked -> save()
        }
    }
}
