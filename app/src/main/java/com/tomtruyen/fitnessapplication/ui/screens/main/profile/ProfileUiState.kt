package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import com.google.firebase.auth.FirebaseUser

data class ProfileUiState(
    val user: FirebaseUser? = null
)
