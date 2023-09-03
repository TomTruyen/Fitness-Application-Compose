package com.tomtruyen.fitnessapplication.repositories

import com.tomtruyen.fitnessapplication.data.dao.SettingsDao
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import kotlinx.coroutines.launch

class SettingsRepositoryImpl(
    private val globalProvider: GlobalProvider,
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
                context = globalProvider.context,
                callback = callback
            ) {
                settings.id = userId

                launchWithTransaction {
                    settingsDao.save(settings)
                }

                callback.onSuccess(settings)
            }
    }

    override fun getSettings(userId: String, callback: FirebaseCallback<Settings>) = tryRequestWhenNotFetched(
        identifier = FetchedData.Type.SETTINGS.identifier,
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.SETTINGS.identifier)
                },
                callback = callback
            ) {
                val settings = (it.toObject(Settings::class.java) ?: Settings()).apply {
                    id = it.id
                }

                launchWithTransaction {
                    settingsDao.save(settings)
                }

                callback.onSuccess(settings)
            }
    }

    companion object {
        private const val COLLECTION_NAME = "settings"
    }
}