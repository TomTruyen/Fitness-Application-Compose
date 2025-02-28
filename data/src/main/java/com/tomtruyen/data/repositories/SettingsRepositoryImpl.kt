package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import io.github.jan.supabase.postgrest.from

class SettingsRepositoryImpl(
    private val settingsDao: com.tomtruyen.data.dao.SettingsDao
) : SettingsRepository() {
    override fun findSettings() = settingsDao.findSettings()

    override suspend fun saveSettings(
        userId: String,
        settings: Settings,
    ) {
        val newSettings = settings.copy(userId = userId)

        supabase.from(Settings.TABLE_NAME).upsert(newSettings)

        launchWithTransaction {
            settingsDao.save(newSettings)
        }
    }

    override suspend fun getSettings(userId: String, refresh: Boolean) = fetch(refresh) {
        supabase.from(Settings.TABLE_NAME)
            .select {
                filter {
                    Settings::userId eq userId
                }
            }
            .decodeSingleOrNull<Settings>()
            ?.let { settings ->
                launchWithCacheTransactions {
                    settingsDao.save(settings)
                }
            }
    }
}