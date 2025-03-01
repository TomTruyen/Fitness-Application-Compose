package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.models.ui.SettingsUiModel
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository : BaseRepository() {
    override val identifier: String
        get() = Settings.TABLE_NAME

    abstract fun findSettings(): Flow<SettingsUiModel?>
    abstract suspend fun saveSettings(userId: String, settings: SettingsUiModel)
    abstract suspend fun getSettings(userId: String, refresh: Boolean)
}