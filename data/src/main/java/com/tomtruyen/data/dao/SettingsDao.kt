package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.Settings
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SettingsDao {
    @Query("SELECT * FROM ${Settings.TABLE_NAME} LIMIT 1")
    abstract fun findSettings(): Flow<Settings?>
    @Upsert
    abstract fun save(settings: Settings)
}