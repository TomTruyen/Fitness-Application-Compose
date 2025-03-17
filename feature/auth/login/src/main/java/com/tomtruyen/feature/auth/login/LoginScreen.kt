package com.tomtruyen.feature.auth.login

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryTextColor
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.TextDivider
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.validation.errorMessage
import com.tomtruyen.core.validation.isValid
import com.tomtruyen.feature.auth.SocialButtons
import com.tomtruyen.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import com.tomtruyen.core.common.R as CommonR

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            LoginUiEvent.Navigate.Home -> {
                navController.navigate(Screen.Workout.Graph) {
                    popUpTo(Screen.Auth.Graph) {
                        inclusive = true
                    }
                }
            }

            LoginUiEvent.Navigate.Register -> {
                navController.navigate(Screen.Auth.Register)
            }
        }
    }

    LoginScreenLayout(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun LoginScreenLayout(
    state: LoginUiState,
    onAction: (LoginUiAction) -> Unit
) {
    val isValid by remember {
        derivedStateOf {
            state.emailValidationResult.isValid() && state.passwordValidationResult.isValid()
        }
    }

    val focusManager = LocalFocusManager.current

    Scaffold {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Normal)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.Small,
                        Alignment.CenterVertically
                    ),
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
                            color = MaterialTheme.colorScheme.secondaryTextColor.value,
                            fontWeight = FontWeight.Normal
                        ),
                    )

                    Spacer(modifier = Modifier.height(Dimens.Large))

                    TextFields.Default(
                        value = state.email,
                        onValueChange = { email ->
                            onAction(LoginUiAction.EmailChanged(email))
                        },
                        placeholder = stringResource(id = CommonR.string.placeholder_email),
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
                        placeholder = stringResource(id = CommonR.string.placeholder_password),
                        error = state.passwordValidationResult.errorMessage(),
                        obscureText = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Buttons.Default(
                        text = stringResource(id = R.string.button_login_with_email),
                        enabled = isValid && !state.loading,
                        onClick = {
                            focusManager.clearFocus()
                            onAction(LoginUiAction.OnLoginClicked)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextDivider(
                        text = stringResource(id = CommonR.string.label_or),
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
                    text = stringResource(id = R.string.button_need_account),
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