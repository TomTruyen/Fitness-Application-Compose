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
): BaseViewModel<ExercisesNavigationType>() {
    val state = MutableStateFlow(
        ExercisesUiState(isFromWorkout = isFromWorkout)
    )

    val exercises = state.flatMapLatest {
        exerciseRepository.findExercises(it.search, it.filter)
    }

    val categories = exerciseRepository.findCategories()
    val equipment = exerciseRepository.findEquipment()

    private val callback by lazy {
        object: FirebaseCallback<List<Exercise>> {
            override fun onSuccess(value: List<Exercise>) {}

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

    fun onEvent(event: ExercisesUiEvent) {
        val currentState = state.value

        when (event) {
            is ExercisesUiEvent.OnToggleSearch -> state.value = currentState.copy(searching = !currentState.searching)
            is ExercisesUiEvent.OnSearchQueryChanged -> state.value = currentState.copy(search = event.query)
            is ExercisesUiEvent.OnCategoryFilterChanged -> state.value = currentState.copy(
                filter = currentState.filter.copy().apply {
                    tryAddCategory(event.category)
                }
            )
            is ExercisesUiEvent.OnEquipmentFilterChanged -> state.value = currentState.copy(
                filter = currentState.filter.copy().apply {
                    tryAddEquipment(event.equipment)
                }
            )
            is ExercisesUiEvent.OnClearFilterClicked -> state.value = currentState.copy(filter = ExerciseFilter())
            is ExercisesUiEvent.OnRemoveFilterClicked -> state.value = currentState.copy(
                filter = currentState.filter.copy(
                    categories = currentState.filter.categories.toMutableList().apply { remove(event.filter) },
                    equipment = currentState.filter.equipment.toMutableList().apply { remove(event.filter) }
                )
            )
            is ExercisesUiEvent.OnFilterClicked -> navigate(ExercisesNavigationType.Filter)
            is ExercisesUiEvent.OnAddClicked -> navigate(ExercisesNavigationType.Add)
            is ExercisesUiEvent.OnExerciseClicked -> {
                if (isFromWorkout) {
                    state.value = currentState.copy(
                        selectedExercise = if (currentState.selectedExercise == event.exercise) null else event.exercise
                    )
                } else {
                    navigate(ExercisesNavigationType.Detail(event.exercise.id))
                }
            }
            is ExercisesUiEvent.OnAddExerciseToWorkoutClicked -> {
                navigate(ExercisesNavigationType.BackToWorkout(event.exercise))
            }
        }
    }

}
