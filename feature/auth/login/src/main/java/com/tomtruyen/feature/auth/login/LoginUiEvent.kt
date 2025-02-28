package com.tomtruyen.feature.auth.login

sealed class LoginUiEvent {
    data object NavigateToHome : LoginUiEvent()

    data object NavigateToRegister : LoginUiEvent()
}
