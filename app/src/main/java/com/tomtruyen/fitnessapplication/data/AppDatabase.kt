package com.tomtruyen.fitnessapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tomtruyen.fitnessapplication.R

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            name = context.getString(R.string.app_name)
        ).build()
    }
}