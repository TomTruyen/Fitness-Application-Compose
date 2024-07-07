package com.tomtruyen.fitnessapplication.di

import com.tomtruyen.feature.auth.login.LoginViewModel
import com.tomtruyen.feature.auth.register.RegisterViewModel
import com.tomtruyen.fitnessapplication.BuildConfig
import com.tomtruyen.fitnessapplication.providers.CredentialProviderImpl
import com.tomtruyen.fitnessapplication.ui.screens.main.profile.ProfileViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.WorkoutOverviewViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder.ReorderWorkoutExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create.CreateExerciseViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail.WorkoutDetailViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute.ExecuteWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history.WorkoutHistoryViewModel
import com.tomtruyen.models.providers.CredentialProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CredentialProvider> {
        CredentialProviderImpl(
            googleServerClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID
        )
    }

    // ViewModels
    viewModelOf(::LoginViewModel)

    viewModelOf(::RegisterViewModel)

    viewModelOf(::ProfileViewModel)

    viewModelOf(::WorkoutOverviewViewModel)

    viewModel { (id: String) ->
        WorkoutDetailViewModel(id, get(), get())
    }

    viewModel { (id: String) ->
        ExecuteWorkoutViewModel(id, get(), get(), get())
    }

    viewModel { (id: String?) ->
        CreateWorkoutViewModel(id, get(), get(), get())
    }

    viewModelOf(::ReorderWorkoutExercisesViewModel)

    viewModel { (isFromWorkout: Boolean) ->
        ExercisesViewModel(isFromWorkout, get(), get())
    }

    viewModel { (id: String) ->
        ExerciseDetailViewModel(id, get(), get())
    }

    viewModel { (id: String?) ->
        CreateExerciseViewModel(id, get(), get())
    }

    viewModelOf(::WorkoutHistoryViewModel)
}