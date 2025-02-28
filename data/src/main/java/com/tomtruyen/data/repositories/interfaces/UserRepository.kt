package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import io.github.jan.supabase.auth.user.UserInfo

abstract class UserRepository : BaseRepository() {
    override val identifier: String
        get() = "users"

    abstract suspend fun login(email: String, password: String)
    abstract suspend fun register(email: String, password: String)
    abstract suspend fun loginWithGoogle(idToken: String)
    abstract suspend fun logout()
    protected abstract suspend fun onAuthenticated()
    abstract fun isLoggedIn(): Boolean
    abstract fun getUser(): UserInfo?
}