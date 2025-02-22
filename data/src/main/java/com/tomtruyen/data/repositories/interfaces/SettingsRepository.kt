package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.FirebaseCallback
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository: BaseRepository() {
    override val identifier: String
        get() = "settings"

    abstract fun findSettings(): Flow<Settings?>
    abstract fun saveSettings(userId: String, settings: Settings, callback: FirebaseCallback<Settings>)
    abstract suspend fun getSettings(userId: String, callback: FirebaseCallback<Settings>)
}