package com.tomtruyen.fitnessapplication.ui.screens.auth.login

sealed class LoginUiEvent {
    data object NavigateToHome: LoginUiEvent()
    data object NavigateToRegister: LoginUiEvent()
}
