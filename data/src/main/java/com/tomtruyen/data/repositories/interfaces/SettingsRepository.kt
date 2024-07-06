package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.models.DataFetchTracker
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository: BaseRepository(DataFetchTracker.SETTINGS) {
    abstract fun findSettings(): Flow<Settings?>
    abstract fun saveSettings(userId: String, settings: Settings, callback: FirebaseCallback<Settings>)
    abstract fun getSettings(userId: String, callback: FirebaseCallback<Settings>)
}