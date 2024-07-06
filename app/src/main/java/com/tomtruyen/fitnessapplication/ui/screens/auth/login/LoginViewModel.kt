package com.tomtruyen.fitnessapplication.ui.screens.auth.login

import com.google.firebase.auth.FirebaseUser
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
): BaseViewModel<LoginUiState, LoginUiAction, LoginUiEvent>(
    initialState = LoginUiState()
) {
    private val callback by lazy {
        object: FirebaseCallback<FirebaseUser?> {
            override fun onSuccess(value: FirebaseUser?) {
                triggerEvent(LoginUiEvent.NavigateToHome)
            }

            override fun onError(error: String?) {
                showSnackbar(SnackbarMessage.Error(error))
            }

            override fun onStopLoading() {
                isLoading(false)
            }
        }
    }

    init {
        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun login() {
        isLoading(true)
        userRepository.login(
            email = uiState.value.email.orEmpty(),
            password = uiState.value.password.orEmpty(),
            callback = callback
        )
    }

    private fun loginWithGoogle(idToken: String) {
        isLoading(true)
        userRepository.loginWithGoogle(idToken, callback)
    }

    override fun onAction(action: LoginUiAction) {
        when(action) {
            is LoginUiAction.EmailChanged -> updateState {
                it.copy(
                    email = action.email,
                    emailValidationResult = it.validateEmail(action.email)
                )
            }
            is LoginUiAction.PasswordChanged -> updateState {
                it.copy(
                    password = action.password,
                    passwordValidationResult = it.validatePassword(action.password)
                )
            }
            is LoginUiAction.OnGoogleSignInFailed -> showSnackbar(SnackbarMessage.Error(action.error))
            is LoginUiAction.OnGoogleSignInSuccess -> loginWithGoogle(action.idToken)
            LoginUiAction.OnLoginClicked -> login()
            LoginUiAction.OnRegisterClicked -> triggerEvent(LoginUiEvent.NavigateToRegister)
        }
    }
}
