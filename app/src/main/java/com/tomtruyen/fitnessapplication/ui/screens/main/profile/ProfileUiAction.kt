package com.tomtruyen.fitnessapplication.ui.screens.main.profile

sealed class ProfileUiAction {
    data class UnitChanged(val value: String) : ProfileUiAction()
    data class RestChanged(val value: Int) : ProfileUiAction()
    data class RestEnabledChanged(val value: Boolean) : ProfileUiAction()
    data class RestVibrationEnabledChanged(val value: Boolean) : ProfileUiAction()
    data object Logout : ProfileUiAction()
}
