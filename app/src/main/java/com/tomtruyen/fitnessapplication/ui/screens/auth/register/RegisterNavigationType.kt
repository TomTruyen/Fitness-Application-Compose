package com.tomtruyen.fitnessapplication.ui.screens.auth.register

sealed class RegisterNavigationType {
    data object Home: RegisterNavigationType()
    data object Login: RegisterNavigationType()
}
