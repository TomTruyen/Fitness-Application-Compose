package com.tomtruyen.fitnessapplication.di

import com.tomtruyen.fitnessapplication.helpers.ContextProvider
import com.tomtruyen.fitnessapplication.repositories.UserRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // ContextProvider - Used to provide context to non-activity classes
    single { ContextProvider(androidContext()) }

    // Repositories
    single<UserRepository> { UserRepositoryImpl(get()) }
}