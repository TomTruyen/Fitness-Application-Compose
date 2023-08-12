package com.tomtruyen.fitnessapplication.ui.screens.auth.login

sealed class LoginNavigationType {
    data object Home: LoginNavigationType()
    data object Register: LoginNavigationType()
}
