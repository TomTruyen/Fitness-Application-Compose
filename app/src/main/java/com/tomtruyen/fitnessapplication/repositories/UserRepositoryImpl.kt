package com.tomtruyen.fitnessapplication.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository

class UserRepositoryImpl: UserRepository {
    private val auth = Firebase.auth
    override fun login() {
        TODO("Not yet implemented")
    }

    override fun register() {
        TODO("Not yet implemented")
    }

    override fun loginWithGoogle() {
        TODO("Not yet implemented")
    }

    override fun logout() {
        auth.signOut()
    }
}