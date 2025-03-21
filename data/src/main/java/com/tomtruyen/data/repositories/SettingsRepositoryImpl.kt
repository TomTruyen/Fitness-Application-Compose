package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.models.mappers.SettingsUiModelMapper
import com.tomtruyen.data.models.ui.SettingsUiModel
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.worker.SettingsSyncWorker
import com.tomtruyen.data.worker.SyncWorker
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class SettingsRepositoryImpl : SettingsRepository() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findSettings() = dao.findSettings().mapLatest { settings ->
        settings?.let(SettingsUiModelMapper::fromEntity)
    }

    override suspend fun saveSettings(
        userId: String,
        settings: SettingsUiModel,
    ) {
        val newSettings = SettingsUiModelMapper.toEntity(
            model = settings
        ).copy(userId = userId)

        dao.save(newSettings)

        SyncWorker.schedule<SettingsSyncWorker>()
    }

    override suspend fun getSettings(userId: String, refresh: Boolean) {
        fetch(refresh) {
            supabase.from(Settings.TABLE_NAME)
                .select {
                    filter {
                        Settings::userId eq userId
                    }
                }
                .decodeSingleOrNull<Settings>()
                ?.let { settings ->
                    cacheTransaction {
                        dao.save(settings)
                    }
                }
        }
    }

    override suspend fun sync(item: Settings) {
        supabase.from(Settings.TABLE_NAME).upsert(item)

        dao.save(item.copy(synced = true))
    }
}