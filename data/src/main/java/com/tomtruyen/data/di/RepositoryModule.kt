package com.tomtruyen.data.di

import com.tomtruyen.data.repositories.ExerciseRepositoryImpl
import com.tomtruyen.data.repositories.SettingsRepositoryImpl
import com.tomtruyen.data.repositories.UserRepositoryImpl
import com.tomtruyen.data.repositories.WorkoutHistoryRepositoryImpl
import com.tomtruyen.data.repositories.WorkoutRepositoryImpl
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    // Repositories
    singleOf<UserRepository>(::UserRepositoryImpl)
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get(), get(), get()) }
    single<WorkoutHistoryRepository> { WorkoutHistoryRepositoryImpl(get(), get()) }
}