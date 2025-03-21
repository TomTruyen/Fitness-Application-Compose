package com.tomtruyen.data.repositories

import com.tomtruyen.core.common.utils.GoogleSignInHelper
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val equipmentRepository: EquipmentRepository,
    private val categoryRepository: CategoryRepository,
    private val exerciseRepository: ExerciseRepository
) : UserRepository() {
    private val auth = supabase.auth

    override suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        onAuthenticated()
    }

    override suspend fun register(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        onAuthenticated()
    }

    override suspend fun loginWithGoogle(idToken: String) {
        auth.signInWith(IDToken) {
            this.idToken = idToken
            provider = Google
        }

        onAuthenticated()
    }

    override suspend fun logout() {
        scope.launch {
            database.clearAllTables()
            GoogleSignInHelper.signOut(context)
        }

        auth.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        onAuthenticated()

        return auth.currentUserOrNull() != null
    }

    override fun getUser() = auth.currentUserOrNull()

    override suspend fun onAuthenticated() {
        auth.awaitInitialization()

        coroutineScope {
            val calls = listOf(
                async { fetchSettings() },
                async { fetchEquipment() },
                async { fetchCategories() },
                async { fetchExercises() }
            )

            calls.awaitAll()
        }
    }

    private suspend fun fetchSettings() = getUser()?.let { user ->
        settingsRepository.getSettings(user.id, false)
    }

    private suspend fun fetchEquipment() = equipmentRepository.getEquipment()

    private suspend fun fetchCategories() = categoryRepository.getCategories()

    private suspend fun fetchExercises() = getUser()?.let { user ->
        exerciseRepository.getExercises(
            userId = user.id,
            refresh = false
        )
    }
}