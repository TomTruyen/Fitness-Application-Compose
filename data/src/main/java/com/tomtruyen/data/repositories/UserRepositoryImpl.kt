package com.tomtruyen.data.repositories

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val database: com.tomtruyen.data.AppDatabase,
    private val settingsRepository: SettingsRepository
): UserRepository() {
    private val auth = Firebase.auth
    override fun login(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>) {
        auth.signInWithEmailAndPassword(email, password)
            .handleCompletionResult(
                context = context,
                callback = callback
            ) { result ->
                getUserData(callback)
                callback.onSuccess(result.user)
            }
    }

    override fun register(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>) {
        auth.createUserWithEmailAndPassword(email, password)
            .handleCompletionResult(
                context = context,
                callback = callback
            ) { result ->
                getUserData(callback)
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
                getUserData(callback)
                callback.onSuccess(result.user)
            }
    }

    override fun logout() {
        scope.launch {
            database.clearAllTables()
        }

        auth.signOut()
    }

    override fun isLoggedIn() = auth.currentUser != null

    override fun getUser() = auth.currentUser

    private fun getUserData(callback: FirebaseCallback<FirebaseUser?>) = scope.launch {
        val userId = getUser()?.uid ?: return@launch callback.onStopLoading()

        settingsRepository.getSettings(
            userId = userId,
            callback = object: FirebaseCallback<Settings> {
                override fun onError(error: String?) {
                    callback.onError(error)
                }
            }
        )
    }
}