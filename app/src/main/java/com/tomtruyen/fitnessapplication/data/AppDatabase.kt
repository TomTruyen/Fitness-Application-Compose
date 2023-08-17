package com.tomtruyen.fitnessapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.dao.ExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.SettingsDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutExerciseDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutSetDao
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.data.entities.Workout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutExercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet

@Database(
    entities = [
        Exercise::class,
        Settings::class,
        Workout::class,
        WorkoutExercise::class,
        WorkoutSet::class,
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
}