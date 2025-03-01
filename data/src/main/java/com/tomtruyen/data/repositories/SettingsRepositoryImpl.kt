package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.SettingsDao
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.models.ui.SettingsUiModel
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
) : SettingsRepository() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findSettings() = settingsDao.findSettings().mapLatest { settings ->
        settings?.let(SettingsUiModel::fromEntity)
    }

    override suspend fun saveSettings(
        userId: String,
        settings: SettingsUiModel,
    ) {
        val newSettings = settings.toEntity().copy(userId = userId)

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