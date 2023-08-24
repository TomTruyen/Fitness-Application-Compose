package com.tomtruyen.fitnessapplication.di

import androidx.room.Room
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.repositories.ExerciseRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.SettingsRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.UserRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.WorkoutRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginViewModel
import com.tomtruyen.fitnessapplication.ui.screens.auth.register.RegisterViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.profile.ProfileViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.WorkoutOverviewViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder.ReorderWorkoutExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail.ExerciseDetailViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create.CreateExerciseViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail.WorkoutDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // GlobalProvider - Used to provide context to non-activity classes + global variables
    single {
        GlobalProvider(context = androidContext())
    }

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
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().workoutDao() }
    single { get<AppDatabase>().workoutExerciseDao() }
    single { get<AppDatabase>().workoutSetDao() }

    // Repositories
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get(), get(), get(), get()) }

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::WorkoutOverviewViewModel)
    viewModelOf(::WorkoutDetailViewModel)
    viewModel { (id: String?) -> CreateWorkoutViewModel(id, get(), get(), get()) }
    viewModelOf(::ReorderWorkoutExercisesViewModel)
    viewModelOf(::ExercisesViewModel)
    viewModelOf(::ExerciseDetailViewModel)
    viewModel { (id: String?) -> CreateExerciseViewModel(id, get(), get()) }
}