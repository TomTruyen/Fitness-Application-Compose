package com.tomtruyen.fitnessapplication.ui.screens.auth.login

sealed class LoginUiEvent {
    data class EmailChanged(val email: String): LoginUiEvent()
    data class PasswordChanged(val password: String): LoginUiEvent()
    data object OnLoginClicked: LoginUiEvent()
    data object OnRegisterClicked: LoginUiEvent()
    data class OnGoogleSignInSuccess(val idToken: String): LoginUiEvent()
    data class OnGoogleSignInFailed(val error: String?): LoginUiEvent()
}