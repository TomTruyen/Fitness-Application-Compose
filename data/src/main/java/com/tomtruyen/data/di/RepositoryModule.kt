package com.tomtruyen.data.di

import com.tomtruyen.data.dao.CategoryDao
import com.tomtruyen.data.dao.EquipmentDao
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.dao.SettingsDao
import com.tomtruyen.data.dao.WorkoutDao
import com.tomtruyen.data.dao.WorkoutExerciseDao
import com.tomtruyen.data.dao.WorkoutHistoryDao
import com.tomtruyen.data.dao.WorkoutSetDao
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

    single<ExerciseRepository> {
        ExerciseRepositoryImpl(
            exerciseDao = get<ExerciseDao>()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsDao = get<SettingsDao>()
        )
    }

    single<WorkoutRepository> {
        // TODO: See if we can limit the amount of parameters passed to the constructor
        WorkoutRepositoryImpl(
            workoutDao = get<WorkoutDao>(),
            workoutExerciseDao = get<WorkoutExerciseDao>(),
            workoutSetDao = get<WorkoutSetDao>(),
            exerciseDao = get<ExerciseDao>()
        )
    }

    single<WorkoutHistoryRepository> {
        WorkoutHistoryRepositoryImpl(
            workoutHistoryDao = get<WorkoutHistoryDao>()
        )
    }

    single<CategoryRepository> {
        CategoryRepositoryImpl(
            categoryDao = get<CategoryDao>()
        )
    }

    single<EquipmentRepository> {
        EquipmentRepositoryImpl(
            equipmentDao = get<EquipmentDao>()
        )
    }
}