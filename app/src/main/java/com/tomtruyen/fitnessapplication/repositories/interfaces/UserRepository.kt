package com.tomtruyen.fitnessapplication.repositories.interfaces

interface UserRepository {
    fun login()
    fun register()
    fun loginWithGoogle()
    fun logout()
}