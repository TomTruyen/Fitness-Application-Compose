package com.tomtruyen.fitnessapplication.ui.screens.auth.login

sealed class LoginUiAction {
    data class EmailChanged(val email: String): LoginUiAction()

    data class PasswordChanged(val password: String): LoginUiAction()

    data object OnLoginClicked: LoginUiAction()

    data object OnRegisterClicked: LoginUiAction()

    data class OnGoogleSignInSuccess(val idToken: String): LoginUiAction()

    data class OnGoogleSignInFailed(val error: String?): LoginUiAction()
}