package com.tomtruyen.fitoryx.di

import com.tomtruyen.feature.auth.login.LoginViewModel
import com.tomtruyen.feature.auth.register.RegisterViewModel
import com.tomtruyen.feature.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.fitoryx.BuildConfig
import com.tomtruyen.feature.profile.ProfileViewModel
import com.tomtruyen.fitoryx.providers.BuildConfigFieldProviderImpl
import com.tomtruyen.fitoryx.providers.KoinReloadProviderImpl
import com.tomtruyen.feature.workouts.WorkoutsViewModel
import com.tomtruyen.feature.workouts.history.WorkoutHistoryViewModel
import com.tomtruyen.fitnessapplication.providers.CredentialProviderImpl
import com.tomtruyen.models.providers.BuildConfigFieldProvider
import com.tomtruyen.models.providers.CredentialProvider
import com.tomtruyen.models.providers.KoinReloadProvider
import com.tomtruyen.navigation.Screen
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.storage.Storage
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val appModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest) {
                propertyConversionMethod = PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE
            }
            install(Storage) {
                transferTimeout = 60.seconds
            }
        }
    }

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

    viewModel { (id: String?, execute: Boolean) ->
        com.tomtruyen.feature.workouts.manage.ManageWorkoutViewModel(id, execute, get(), get(), get(), get())
    }

    viewModel { (mode: Screen.Exercise.Overview.Mode) ->
        com.tomtruyen.feature.exercises.ExercisesViewModel(mode, get(), get())
    }

    viewModel { (id: String) ->
        ExerciseDetailViewModel(id, get(), get())
    }

    viewModel { (id: String?) ->
        com.tomtruyen.feature.exercises.manage.ManageExerciseViewModel(id, get(), get())
    }

    viewModelOf(::WorkoutHistoryViewModel)
}