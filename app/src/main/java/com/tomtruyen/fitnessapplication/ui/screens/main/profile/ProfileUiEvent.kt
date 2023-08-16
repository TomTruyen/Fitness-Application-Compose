package com.tomtruyen.fitnessapplication.ui.screens.main.profile

sealed class ProfileUiEvent {
    data class UnitChanged(val value: String) : ProfileUiEvent()
    data class RestChanged(val value: Int) : ProfileUiEvent()
    data class RestEnabledChanged(val value: Boolean) : ProfileUiEvent()
    data class RestVibrationEnabledChanged(val value: Boolean) : ProfileUiEvent()
    data object Logout : ProfileUiEvent()
}
