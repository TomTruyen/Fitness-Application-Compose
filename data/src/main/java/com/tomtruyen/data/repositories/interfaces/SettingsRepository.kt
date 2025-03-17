package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.models.ui.SettingsUiModel
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository : SyncRepository<Settings>() {
    override val cacheKey: String
        get() = Settings.TABLE_NAME

    override val dao = database.settingsDao()

    abstract fun findSettings(): Flow<SettingsUiModel?>
    abstract suspend fun saveSettings(userId: String, settings: SettingsUiModel)
    abstract suspend fun getSettings(userId: String, refresh: Boolean)
}