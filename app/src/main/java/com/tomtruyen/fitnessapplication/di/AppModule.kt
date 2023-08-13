package com.tomtruyen.fitnessapplication.di

import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.helpers.ContextProvider
import com.tomtruyen.fitnessapplication.repositories.UserRepositoryImpl
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginViewModel
import com.tomtruyen.fitnessapplication.ui.screens.auth.register.RegisterViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.profile.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // ContextProvider - Used to provide context to non-activity classes
    single { ContextProvider(androidContext()) }

    // Database
    single { AppDatabase.createDatabase(androidContext()) }

    // Repositories
    single<UserRepository> { UserRepositoryImpl(get()) }

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ProfileViewModel)
}