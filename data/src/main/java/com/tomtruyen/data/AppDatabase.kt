package com.tomtruyen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomtruyen.data.dao.CacheTTLDao
import com.tomtruyen.data.dao.CategoryDao
import com.tomtruyen.data.dao.EquipmentDao
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.dao.SettingsDao
import com.tomtruyen.data.dao.WorkoutDao
import com.tomtruyen.data.dao.WorkoutExerciseDao
import com.tomtruyen.data.dao.WorkoutExerciseSetDao
import com.tomtruyen.data.dao.WorkoutHistoryDao
import com.tomtruyen.data.entities.CacheTTL
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutHistory

@Database(
    entities = [
        Exercise::class,
        Settings::class,
        Workout::class,
        WorkoutExercise::class,
        WorkoutExerciseSet::class,
        WorkoutHistory::class,
        CacheTTL::class,
        Equipment::class,
        Category::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun settingsDao(): SettingsDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutExerciseSetDao(): WorkoutExerciseSetDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun cacheTTLDao(): CacheTTLDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun categoryDao(): CategoryDao
}