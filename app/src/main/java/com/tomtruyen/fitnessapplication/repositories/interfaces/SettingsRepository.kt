package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import kotlinx.coroutines.flow.Flow

abstract class SettingsRepository: BaseRepository() {
    abstract fun findSettings(): Flow<Settings?>
    abstract fun saveSettings(userId: String, settings: Settings, callback: FirebaseCallback<Settings>)
    abstract fun getSettings(userId: String, callback: FirebaseCallback<Settings>)
}