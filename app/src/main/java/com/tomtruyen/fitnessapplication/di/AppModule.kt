package com.tomtruyen.fitnessapplication.di

import androidx.room.Room
import com.tomtruyen.fitnessapplication.AppGlobals
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.helpers.ContextProvider
import com.tomtruyen.fitnessapplication.repositories.ExerciseRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.UserRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginViewModel
import com.tomtruyen.fitnessapplication.ui.screens.auth.register.RegisterViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.profile.ProfileViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create.CreateExerciseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // ContextProvider - Used to provide context to non-activity classes
    single { ContextProvider(androidContext()) }

    // Globals
    single { AppGlobals() }

    // Database
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            name = androidContext().getString(R.string.app_name)
        ).build()
    }

    // Dao
    single { get<AppDatabase>().exerciseDao() }

    // Repositories
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get(), get()) }

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ExercisesViewModel)
    viewModelOf(::ExerciseDetailViewModel)
    viewModel { (id: String?) -> CreateExerciseViewModel(id, get(), get(), get()) }
}