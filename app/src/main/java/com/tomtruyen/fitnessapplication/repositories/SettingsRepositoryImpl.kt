package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.FetchedData
import com.tomtruyen.fitnessapplication.data.dao.SettingsDao
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.ContextProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsRepositoryImpl(
    private val contextProvider: ContextProvider,
    private val settingsDao: SettingsDao
): SettingsRepository() {
    override fun findSettings() = settingsDao.findSettings()

    override fun saveSettings(
        userId: String,
        settings: Settings,
        callback: FirebaseCallback<Settings>
    ) {
        db.collection(COLLECTION_NAME)
            .document(userId)
            .set(settings)
            .handleCompletionResult(
                context = contextProvider.context,
                callback = callback
            ) {
                settings.id = userId

                scope.launch {
                    settingsDao.save(settings)
                }

                callback.onSuccess(settings)
            }
    }

    override fun getSettings(userId: String, callback: FirebaseCallback<Settings>) {
        db.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = contextProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.SETTINGS)
                },
                callback = callback
            ) {
                val settings = it.toObject(Settings::class.java) ?: Settings()

                settings.id = it.id

                scope.launch {
                    settingsDao.save(settings)
                }

                callback.onSuccess(settings)
            }
    }

    companion object {
        private const val COLLECTION_NAME = "settings"
    }
}