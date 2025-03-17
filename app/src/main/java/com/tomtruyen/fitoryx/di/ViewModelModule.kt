package com.tomtruyen.fitoryx.di

import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.auth.login.LoginViewModel
import com.tomtruyen.feature.auth.register.RegisterViewModel
import com.tomtruyen.feature.exercises.ExercisesViewModel
import com.tomtruyen.feature.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.feature.exercises.manage.ManageExerciseViewModel
import com.tomtruyen.feature.profile.ProfileViewModel
import com.tomtruyen.feature.workouts.WorkoutsViewModel
import com.tomtruyen.feature.workouts.history.WorkoutHistoryViewModel
import com.tomtruyen.feature.workouts.history.detail.WorkoutHistoryDetailViewModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutViewModel
import com.tomtruyen.feature.workouts.manage.reorder.ReorderExercisesViewModel
import com.tomtruyen.fitoryx.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)

    viewModelOf(::LoginViewModel)

    viewModelOf(::RegisterViewModel)

    viewModelOf(::ProfileViewModel)

    viewModelOf(::WorkoutsViewModel)

    viewModel { (id: String?, mode: WorkoutMode, workout: WorkoutUiModel?) ->
        ManageWorkoutViewModel(
            id = id,
            mode = mode,
            workout = workout,
            userRepository = get<UserRepository>(),
            workoutRepository = get<WorkoutRepository>(),
            historyRepository = get<HistoryRepository>(),
            previousSetRepository = get<PreviousSetRepository>(),
            settingsRepository = get<SettingsRepository>(),
        )
    }

    viewModel { (exercises: List<WorkoutExerciseUiModel>) ->
        ReorderExercisesViewModel(
            exercises = exercises
        )
    }

    viewModel { (mode: ExerciseMode) ->
        ExercisesViewModel(
            mode = mode,
            exerciseRepository = get<ExerciseRepository>(),
            categoryRepository = get<CategoryRepository>(),
            equipmentRepository = get<EquipmentRepository>(),
            userRepository = get<UserRepository>()
        )
    }

    viewModel { (id: String) ->
        ExerciseDetailViewModel(
            id = id,
            exerciseRepository = get<ExerciseRepository>(),
            userRepository = get<UserRepository>()
        )
    }

    viewModel { (id: String?) ->
        ManageExerciseViewModel(
            id = id,
            exerciseRepository = get<ExerciseRepository>(),
            categoryRepository = get<CategoryRepository>(),
            equipmentRepository = get<EquipmentRepository>(),
            userRepository = get<UserRepository>()
        )
    }

    viewModelOf(::WorkoutHistoryViewModel)

    viewModel { (id: String) ->
        WorkoutHistoryDetailViewModel(
            id = id,
            historyRepository = get<HistoryRepository>()
        )
    }
}