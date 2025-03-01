package com.tomtruyen.data.di

import com.tomtruyen.data.repositories.CategoryRepositoryImpl
import com.tomtruyen.data.repositories.EquipmentRepositoryImpl
import com.tomtruyen.data.repositories.ExerciseRepositoryImpl
import com.tomtruyen.data.repositories.SettingsRepositoryImpl
import com.tomtruyen.data.repositories.UserRepositoryImpl
import com.tomtruyen.data.repositories.WorkoutHistoryRepositoryImpl
import com.tomtruyen.data.repositories.WorkoutRepositoryImpl
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    // Repositories
    single<UserRepository> {
        UserRepositoryImpl(
            settingsRepository = get<SettingsRepository>(),
            categoryRepository = get<CategoryRepository>(),
            equipmentRepository = get<EquipmentRepository>()
        )
    }

    singleOf<ExerciseRepository>(::ExerciseRepositoryImpl)

    singleOf<SettingsRepository>(::SettingsRepositoryImpl)

    singleOf<WorkoutRepository>(::WorkoutRepositoryImpl)

    singleOf<WorkoutHistoryRepository>(::WorkoutHistoryRepositoryImpl)

    singleOf<CategoryRepository>(::CategoryRepositoryImpl)

    singleOf<EquipmentRepository>(::EquipmentRepositoryImpl)
}