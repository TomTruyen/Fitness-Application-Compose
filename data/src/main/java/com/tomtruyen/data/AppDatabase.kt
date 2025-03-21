package com.tomtruyen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomtruyen.data.dao.CategoryDao
import com.tomtruyen.data.dao.EquipmentDao
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.dao.ExerciseRecordDao
import com.tomtruyen.data.dao.PreviousSetDao
import com.tomtruyen.data.dao.SettingsDao
import com.tomtruyen.data.dao.SyncCacheDao
import com.tomtruyen.data.dao.WorkoutDao
import com.tomtruyen.data.dao.WorkoutExerciseDao
import com.tomtruyen.data.dao.WorkoutExerciseSetDao
import com.tomtruyen.data.dao.WorkoutHistoryDao
import com.tomtruyen.data.dao.WorkoutHistoryExerciseDao
import com.tomtruyen.data.dao.WorkoutHistoryExerciseSetDao
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseRecord
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.entities.SyncCache
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet

@Database(
    entities = [
        Exercise::class,
        Equipment::class,
        Category::class,
        Workout::class,
        WorkoutExercise::class,
        WorkoutExerciseSet::class,
        WorkoutHistory::class,
        WorkoutHistoryExercise::class,
        WorkoutHistoryExerciseSet::class,
        Settings::class,
        SyncCache::class,
        PreviousSet::class,
        ExerciseRecord::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun categoryDao(): CategoryDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutExerciseSetDao(): WorkoutExerciseSetDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun workoutHistoryExerciseDao(): WorkoutHistoryExerciseDao
    abstract fun workoutHistoryExerciseSetDao(): WorkoutHistoryExerciseSetDao
    abstract fun settingsDao(): SettingsDao
    abstract fun cacheSyncDao(): SyncCacheDao
    abstract fun previousSetDao(): PreviousSetDao
    abstract fun exerciseRecordDao(): ExerciseRecordDao
}