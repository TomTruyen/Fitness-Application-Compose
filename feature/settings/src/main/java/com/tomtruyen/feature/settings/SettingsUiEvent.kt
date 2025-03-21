package com.tomtruyen.feature.settings

sealed class SettingsUiEvent {
    data object Logout : SettingsUiEvent()
}
