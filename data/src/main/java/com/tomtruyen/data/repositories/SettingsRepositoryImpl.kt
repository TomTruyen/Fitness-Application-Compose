package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsDao: com.tomtruyen.data.dao.SettingsDao
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
                context = context,
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
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(COLLECTION_NAME)
            .document(userId)
            .get()
            .handleCompletionResult(
                context = context,
                setFetchSuccessful = ::setFetchSuccessful,
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