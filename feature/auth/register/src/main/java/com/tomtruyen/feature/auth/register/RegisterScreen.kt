package com.tomtruyen.feature.auth.register

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.secondaryTextColor
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.validation.errorMessage
import com.tomtruyen.core.validation.isValid
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import com.tomtruyen.core.common.R as CommonR

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                RegisterUiEvent.NavigateToHome -> {
                    navController.navigate(Screen.Workout.Graph) {
                        popUpTo(Screen.Auth.Graph) {
                            inclusive = true
                        }
                    }
                }

                RegisterUiEvent.NavigateToLogin -> {
                    navController.navigate(Screen.Auth.Login)
                }
            }
        }
    }

    RegisterScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: RegisterUiState,
    onAction: (RegisterUiAction) -> Unit
) {
    val isValid by remember(state) {
        derivedStateOf {
            state.emailValidationResult.isValid()
                    && state.passwordValidationResult.isValid()
                    && state.confirmPasswordValidationResult.isValid()
        }
    }

    Scaffold(
        snackbarHost = snackbarHost
    ) {
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
                        text = stringResource(id = R.string.title_register),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )

                    Text(
                        text = stringResource(id = R.string.subtitle_register),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.secondaryTextColor,
                            fontWeight = FontWeight.Normal
                        ),
                    )

                    Spacer(modifier = Modifier.height(Dimens.Large))

                    TextFields.Default(
                        value = state.email,
                        onValueChange = { email ->
                            onAction(RegisterUiAction.EmailChanged(email))
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
                            onAction(RegisterUiAction.PasswordChanged(password))
                        },
                        placeholder = stringResource(id = CommonR.string.placeholder_password),
                        error = state.passwordValidationResult.errorMessage(),
                        obscureText = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextFields.Default(
                        value = state.confirmPassword,
                        onValueChange = { confirmPassword ->
                            onAction(RegisterUiAction.ConfirmPasswordChanged(confirmPassword))
                        },
                        placeholder = stringResource(id = CommonR.string.placeholder_password_repeat),
                        error = state.confirmPasswordValidationResult.errorMessage(),
                        obscureText = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    Buttons.Default(
                        text = stringResource(id = R.string.button_register_with_email),
                        enabled = isValid && !state.loading,
                        onClick = {
                            onAction(RegisterUiAction.OnRegisterClicked)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Buttons.Text(
                    text = stringResource(id = R.string.button_have_an_account),
                    enabled = !state.loading,
                    onClick = {
                        onAction(RegisterUiAction.OnLoginClicked)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
