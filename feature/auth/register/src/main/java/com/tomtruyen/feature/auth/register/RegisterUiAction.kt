package com.tomtruyen.feature.auth.register


sealed class RegisterUiAction {
    data class EmailChanged(val email: String): RegisterUiAction()

    data class PasswordChanged(val password: String): RegisterUiAction()

    data class ConfirmPasswordChanged(val confirmPassword: String): RegisterUiAction()

    data object OnLoginClicked: RegisterUiAction()

    data object OnRegisterClicked: RegisterUiAction()
}