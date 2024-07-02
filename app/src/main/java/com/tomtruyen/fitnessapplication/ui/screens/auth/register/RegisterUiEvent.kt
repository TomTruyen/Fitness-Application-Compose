package com.tomtruyen.fitnessapplication.ui.screens.auth.register

sealed class RegisterUiEvent {
    data object NavigateToHome: RegisterUiEvent()
    data object NavigateToLogin: RegisterUiEvent()
}
