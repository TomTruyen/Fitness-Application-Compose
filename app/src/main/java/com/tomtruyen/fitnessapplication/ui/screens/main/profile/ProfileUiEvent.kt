package com.tomtruyen.fitnessapplication.ui.screens.main.profile

sealed class ProfileUiEvent {
    data object Logout : ProfileUiEvent()
}
