package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.fitnessapplication.model.FirebaseCallback

interface UserRepository {
    fun login(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    fun register(email: String, password: String, callback: FirebaseCallback<FirebaseUser?>)
    fun loginWithGoogle(idToken: String, callback: FirebaseCallback<FirebaseUser?>)
    fun logout()
    fun isLoggedIn(): Boolean
    fun getUser(): FirebaseUser?
}