package com.tomtruyen.data.di

import androidx.room.Room
import com.tomtruyen.data.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    // Database
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            name = "fitoryx.db"
        ).fallbackToDestructiveMigration().build()
    }

    // Dao
    single { get<AppDatabase>().exerciseDao() }
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().workoutDao() }
    single { get<AppDatabase>().workoutExerciseDao() }
    single { get<AppDatabase>().workoutExerciseSetDao() }
    single { get<AppDatabase>().workoutHistoryDao() }
    single { get<AppDatabase>().cacheSyncDao() }
    single { get<AppDatabase>().equipmentDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().previousSetDao() }
    single { get<AppDatabase>().exerciseRecordDao() }
}