package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.fitnessapplication.base.BaseRepository
import com.tomtruyen.fitnessapplication.model.FirebaseCallback

abstract class UserRepository: BaseRepository() {
    abstract fun login(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun register(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun loginWithGoogle(idToken: String, callback: FirebaseCallback<FirebaseUser?>)
    abstract fun logout()
    abstract fun isLoggedIn(): Boolean
    abstract fun getUser(): FirebaseUser?
}