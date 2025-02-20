package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM ${Settings.TABLE_NAME} LIMIT 1")
    fun findSettings(): Flow<Settings?>
    @Upsert
    fun save(settings: Settings)
}