package com.tomtruyen.data.repositories.interfaces

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.firebase.models.FirebaseCallback

abstract class UserRepository: BaseRepository() {
    override val identifier: String
        get() = "users"

    abstract fun login(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun register(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun loginWithGoogle(idToken: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun logout()
    abstract fun isLoggedIn(): Boolean
    abstract fun getUser(): FirebaseUser?
}