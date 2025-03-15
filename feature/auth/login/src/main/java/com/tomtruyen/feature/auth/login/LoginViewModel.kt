package com.tomtruyen.feature.auth.login

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.controller.SnackbarController
import com.tomtruyen.core.common.controller.SnackbarMessage
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : BaseViewModel<LoginUiState, LoginUiAction, LoginUiEvent>(
    initialState = LoginUiState()
) {
    init {
        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun login() = launchLoading {
        userRepository.login(
            email = uiState.value.email.orEmpty(),
            password = uiState.value.password.orEmpty(),
        )

        triggerEvent(LoginUiEvent.Navigate.Home)
    }

    private fun loginWithGoogle(idToken: String) = launchLoading {
        userRepository.loginWithGoogle(idToken)
        triggerEvent(LoginUiEvent.Navigate.Home)
    }

    override fun onAction(action: LoginUiAction) {
        when (action) {
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

            is LoginUiAction.OnGoogleSignInFailed -> SnackbarController.showSnackbar(
                SnackbarMessage.Error(action.error.orEmpty())
            )

            is LoginUiAction.OnGoogleSignInSuccess -> loginWithGoogle(action.idToken)
            LoginUiAction.OnLoginClicked -> login()
            LoginUiAction.OnRegisterClicked -> triggerEvent(LoginUiEvent.Navigate.Register)
        }
    }
}
