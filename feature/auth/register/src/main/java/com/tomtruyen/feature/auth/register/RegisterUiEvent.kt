package com.tomtruyen.feature.auth.register

sealed class RegisterUiEvent {
    data object NavigateToHome: RegisterUiEvent()

    data object NavigateToLogin: RegisterUiEvent()
}
