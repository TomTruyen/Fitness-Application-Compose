package com.tomtruyen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.dao.SettingsDao
import com.tomtruyen.data.dao.WorkoutDao
import com.tomtruyen.data.dao.WorkoutExerciseDao
import com.tomtruyen.data.dao.WorkoutHistoryDao
import com.tomtruyen.data.dao.WorkoutSetDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutSet

@Database(
    entities = [
        Exercise::class,
        Settings::class,
        Workout::class,
        WorkoutExercise::class,
        WorkoutSet::class,
        WorkoutHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun settingsDao(): SettingsDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
}