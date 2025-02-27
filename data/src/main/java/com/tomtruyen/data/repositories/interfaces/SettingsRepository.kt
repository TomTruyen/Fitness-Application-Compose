package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Settings
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository: BaseRepository() {
    override val identifier: String
        get() = "settings"

    abstract fun findSettings(): Flow<Settings?>
    abstract suspend fun saveSettings(userId: String, settings: Settings)
    abstract suspend fun getSettings(userId: String, refresh: Boolean)
}