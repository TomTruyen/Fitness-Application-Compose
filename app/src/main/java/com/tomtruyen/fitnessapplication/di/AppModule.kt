package com.tomtruyen.fitnessapplication.di

import com.tomtruyen.feature.auth.login.LoginViewModel
import com.tomtruyen.feature.auth.register.RegisterViewModel
import com.tomtruyen.feature.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.fitnessapplication.BuildConfig
import com.tomtruyen.fitnessapplication.providers.CredentialProviderImpl
import com.tomtruyen.feature.profile.ProfileViewModel
import com.tomtruyen.fitnessapplication.providers.BuildConfigFieldProviderImpl
import com.tomtruyen.fitnessapplication.providers.KoinReloadProviderImpl
import com.tomtruyen.feature.workouts.WorkoutsViewModel
import com.tomtruyen.feature.workouts.create.reorder.ReorderWorkoutExercisesViewModel
import com.tomtruyen.feature.workouts.history.WorkoutHistoryViewModel
import com.tomtruyen.models.providers.BuildConfigFieldProvider
import com.tomtruyen.models.providers.CredentialProvider
import com.tomtruyen.models.providers.KoinReloadProvider
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {
    single<CredentialProvider> {
        CredentialProviderImpl(
            googleServerClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID
        )
    }

    single<BuildConfigFieldProvider> {
        BuildConfigFieldProviderImpl(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )
    }

    single<KoinReloadProvider> {
        KoinReloadProviderImpl()
    }

    // ViewModels
    viewModelOf(::LoginViewModel)

    viewModelOf(::RegisterViewModel)

    viewModelOf(::ProfileViewModel)

    viewModelOf(::WorkoutsViewModel)

    viewModel { (id: String) ->
        com.tomtruyen.feature.workouts.detail.WorkoutDetailViewModel(id, get(), get())
    }

    viewModel { (id: String) ->
        com.tomtruyen.feature.workouts.execute.ExecuteWorkoutViewModel(id, get(), get(), get())
    }

    viewModel { (id: String?) ->
        com.tomtruyen.feature.workouts.create.CreateWorkoutViewModel(id, get(), get(), get())
    }

    viewModelOf(::ReorderWorkoutExercisesViewModel)

    viewModel { (isFromWorkout: Boolean) ->
        com.tomtruyen.feature.exercises.ExercisesViewModel(isFromWorkout, get(), get())
    }

    viewModel { (id: String) ->
        ExerciseDetailViewModel(id, get(), get())
    }

    viewModel { (id: String?) ->
        com.tomtruyen.feature.exercises.create.CreateExerciseViewModel(id, get(), get())
    }

    viewModelOf(::WorkoutHistoryViewModel)
}