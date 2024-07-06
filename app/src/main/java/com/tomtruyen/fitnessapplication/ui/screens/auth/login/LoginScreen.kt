package com.tomtruyen.fitnessapplication.ui.screens.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.SocialButtons
import com.tomtruyen.fitnessapplication.ui.shared.TextDivider
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.core.designsystem.theme.BlueGrey
import com.tomtruyen.core.validation.errorMessage
import com.tomtruyen.core.validation.isValid
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                LoginUiEvent.NavigateToHome -> {
                    navController.navigate(Screen.Workout.Graph) {
                        popUpTo(Screen.Auth.Graph) {
                            inclusive = true
                        }
                    }
                }
                LoginUiEvent.NavigateToRegister -> {
                    navController.navigate(Screen.Auth.Register)
                }
            }
        }
    }

    LoginScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: LoginUiState,
    onAction: (LoginUiAction) -> Unit
) {
    val isValid by remember(state) {
        derivedStateOf {
            state.emailValidationResult.isValid() && state.passwordValidationResult.isValid()
        }
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = snackbarHost
    ) {
        BoxWithLoader(
            loading = state.loading,
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Normal)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.CenterVertically),
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(id = R.string.title_login),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )

                    Text(
                        text = stringResource(id = R.string.subtitle_login),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = BlueGrey,
                            fontWeight = FontWeight.Normal
                        ),
                    )

                    Spacer(modifier = Modifier.height(Dimens.Large))

                    TextFields.Default(
                        value = state.email,
                        onValueChange = { email ->
                            onAction(LoginUiAction.EmailChanged(email))
                        },
                        placeholder = stringResource(id = R.string.email),
                        error = state.emailValidationResult.errorMessage(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextFields.Default(
                        value = state.password,
                        onValueChange = { password ->
                            onAction(LoginUiAction.PasswordChanged(password))
                        },
                        placeholder = stringResource(id = R.string.password),
                        error = state.passwordValidationResult.errorMessage(),
                        obscureText = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Buttons.Default(
                        text = stringResource(id = R.string.login),
                        enabled = isValid && !state.loading,
                        onClick = {
                            focusManager.clearFocus()
                            onAction(LoginUiAction.OnLoginClicked)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextDivider(
                        text = stringResource(id = R.string.label_or),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.Normal)
                    )

                    SocialButtons.Google(
                        text = stringResource(id = R.string.button_login_google),
                        enabled = !state.loading,
                        onClick = focusManager::clearFocus,
                        onSuccess = { idToken ->
                            onAction(LoginUiAction.OnGoogleSignInSuccess(idToken))
                        },
                        onError = { error ->
                            onAction(LoginUiAction.OnGoogleSignInFailed(error))
                        }
                    )
                }

                Buttons.Text(
                    text = stringResource(id = R.string.need_account),
                    enabled = !state.loading,
                    onClick = {
                        onAction(LoginUiAction.OnRegisterClicked)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}