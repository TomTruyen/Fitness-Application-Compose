package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Settings
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
//        db.collection(COLLECTION_NAME)
//            .document(userId)
//            .set(settings)
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                val userSettings = settings.copy(id = userId)
//
//                launchWithTransaction {
//                    settingsDao.save(userSettings)
//                }
//
//                callback.onSuccess(userSettings)
//            }
    }

    override suspend fun getSettings(userId: String, refresh: Boolean, callback: FirebaseCallback<Settings>) = fetch(
        refresh = refresh,
        onStopLoading = callback::onStopLoading
    ) {
//        db.collection(COLLECTION_NAME)
//            .document(userId)
//            .get()
//            .handleCompletionResult(
//                context = context,
//                callback = callback
//            ) {
//                val settings = (it.toObject(Settings::class.java) ?: Settings()).copy(
//                    id = it.id
//                )
//
//                launchWithCacheTransactions {
//                    settingsDao.save(settings)
//                }
//
//                callback.onSuccess(settings)
//            }
    }

    companion object {
        private const val COLLECTION_NAME = "settings"
    }
}