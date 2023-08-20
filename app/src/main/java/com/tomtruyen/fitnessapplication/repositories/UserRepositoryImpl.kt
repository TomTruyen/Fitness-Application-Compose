package com.tomtruyen.fitnessapplication.repositories

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.di.appModule
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.SettingsRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class UserRepositoryImpl(
    globalProvider: GlobalProvider,
    private val database: AppDatabase,
    private val settingsRepository: SettingsRepository
): UserRepository() {
    private val context = globalProvider.context

    private val auth = Firebase.auth
    override fun login(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>) {
        auth.signInWithEmailAndPassword(email, password)
            .handleCompletionResult(
                context = context,
                callback = callback
            ) { result ->
                getUserData()
                callback.onSuccess(result.user)
            }
    }

    override fun register(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>) {
        auth.createUserWithEmailAndPassword(email, password)
            .handleCompletionResult(
                context = context,
                callback = callback
            ) { result ->
                getUserData()
                callback.onSuccess(result.user)
            }
    }

    override fun loginWithGoogle(idToken: String, callback: FirebaseCallback<FirebaseUser?>) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .handleCompletionResult(
                context = context,
                callback = callback
            ) { result ->
                getUserData()
                callback.onSuccess(result.user)
            }
    }

    override fun logout() {
        scope.launch {
            database.clearAllTables()
        }

        auth.signOut()

        // Reset Koin
        unloadKoinModules(appModule)
        loadKoinModules(appModule)
    }

    override fun isLoggedIn() = auth.currentUser != null

    override fun getUser() = auth.currentUser

    private fun getUserData() = scope.launch {
        val userId = getUser()?.uid ?: return@launch

        settingsRepository.getSettings(
            userId = userId,
            callback = object: FirebaseCallback<Settings> {
                override fun onSuccess(value: Settings) {}
                override fun onError(error: String?) {}
            }
        )
    }
}