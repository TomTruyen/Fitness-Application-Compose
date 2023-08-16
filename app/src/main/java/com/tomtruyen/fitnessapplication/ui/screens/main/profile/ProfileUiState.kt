package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.fitnessapplication.data.entities.Settings

data class ProfileUiState(
    val initialSettings: Settings? = null,
    val settings: Settings = Settings()
)
