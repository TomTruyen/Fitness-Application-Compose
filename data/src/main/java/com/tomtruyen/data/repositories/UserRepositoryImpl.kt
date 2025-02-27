package com.tomtruyen.data.repositories

import com.tomtruyen.data.firebase.auth.GoogleSignInHelper
import com.tomtruyen.data.repositories.interfaces.UserRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch

class UserRepositoryImpl: UserRepository() {
    private val auth = supabase.auth

    override suspend fun login(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun register(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        login(email, password)
    }

    override suspend fun loginWithGoogle(idToken: String) {
        auth.signInWith(IDToken) {
            this.idToken = idToken
            provider = Google
        }
    }

    override suspend fun logout() {
        scope.launch {
            database.clearAllTables()
            GoogleSignInHelper.signOut(context)
        }

        auth.signOut()
    }

    override fun isLoggedIn() = auth.currentUserOrNull() != null

    override fun getUser() = auth.currentUserOrNull()
}