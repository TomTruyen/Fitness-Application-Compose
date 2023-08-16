package com.tomtruyen.fitnessapplication.ui.screens.main.profile

sealed class ProfileUiEvent {
    data class RestEnabledChanged(val value: Boolean) : ProfileUiEvent()
    data class RestVibrationEnabledChanged(val value: Boolean) : ProfileUiEvent()
    data object Logout : ProfileUiEvent()
}
