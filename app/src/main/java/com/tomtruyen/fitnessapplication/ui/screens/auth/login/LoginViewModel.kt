package com.tomtruyen.fitnessapplication.ui.screens.auth.login

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel(
    private val userRepository: UserRepository
): BaseViewModel<LoginNavigationType>() {
    val state = MutableStateFlow(LoginUiState())

    private val callback by lazy {
        object: FirebaseCallback<FirebaseUser?> {
            override fun onSuccess(value: FirebaseUser?) {
                navigate(LoginNavigationType.Home)
            }

            override fun onError(error: String?) {
                showSnackbar(SnackbarMessage.Error(error))
            }

            override fun onStopLoading() {
                isLoading(false)
            }
        }
    }

    private fun login() {
        isLoading(true)
        userRepository.login(
            email = state.value.email ?: "",
            password = state.value.password ?: "",
            callback = callback
        )
    }

    private fun loginWithGoogle(idToken: String) {
        isLoading(true)
        userRepository.loginWithGoogle(idToken, callback)
    }

    fun onEvent(event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.EmailChanged -> {
                state.value = state.value.copy(email = event.email)
            }
            is LoginUiEvent.PasswordChanged -> {
                state.value = state.value.copy(password = event.password)
            }
            is LoginUiEvent.OnLoginClicked -> {
                login()
            }
            is LoginUiEvent.OnGoogleSignInSuccess -> {
                loginWithGoogle(idToken = event.idToken)
            }
            is LoginUiEvent.OnGoogleSignInFailed -> {
                showSnackbar(SnackbarMessage.Error(event.error))
            }
            is LoginUiEvent.OnRegisterClicked -> {
                navigate(LoginNavigationType.Register)
            }
        }
    }
}
