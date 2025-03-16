package com.tomtruyen.data.di

import com.tomtruyen.data.repositories.CategoryRepositoryImpl
import com.tomtruyen.data.repositories.EquipmentRepositoryImpl
import com.tomtruyen.data.repositories.ExerciseRepositoryImpl
import com.tomtruyen.data.repositories.SettingsRepositoryImpl
import com.tomtruyen.data.repositories.UserRepositoryImpl
import com.tomtruyen.data.repositories.HistoryRepositoryImpl
import com.tomtruyen.data.repositories.WorkoutRepositoryImpl
import com.tomtruyen.data.repositories.PreviousSetRepositoryImpl
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    // Repositories
    single<UserRepository> {
        UserRepositoryImpl(
            settingsRepository = get<SettingsRepository>(),
            categoryRepository = get<CategoryRepository>(),
            equipmentRepository = get<EquipmentRepository>(),
            exerciseRepository = get<ExerciseRepository>()
        )
    }

    single<ExerciseRepository> {
        ExerciseRepositoryImpl(
            previousSetRepository = get<PreviousSetRepository>()
        )
    }

    singleOf<PreviousSetRepository>(::PreviousSetRepositoryImpl)

    singleOf<SettingsRepository>(::SettingsRepositoryImpl)

    singleOf<WorkoutRepository>(::WorkoutRepositoryImpl)

    singleOf<HistoryRepository>(::HistoryRepositoryImpl)

    singleOf<CategoryRepository>(::CategoryRepositoryImpl)

    singleOf<EquipmentRepository>(::EquipmentRepositoryImpl)
}