package com.tomtruyen.fitnessapplication.ui.screens.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.ui.screens.destinations.LoginScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.RegisterScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.validation.errorMessage
import com.tomtruyen.fitnessapplication.validation.isValid
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(state.email) {
        state.validateEmail(context)
    }

    LaunchedEffect(state.password) {
        state.validatePassword(context)
    }

    LaunchedEffect(state.confirmPassword) {
        state.validateConfirmPassword(context)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is RegisterNavigationType.Home -> {
                    navController.navigateAndClearBackStack(WorkoutOverviewScreenDestination)
                }
                is RegisterNavigationType.Login -> {
                    navController.navigateAndClearBackStack(LoginScreenDestination)
                }
            }
        }
    }

    RegisterScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        state = state,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun RegisterScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: RegisterUiState,
    loading: Boolean,
    onEvent: (RegisterUiEvent) -> Unit
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
        BoxWithLoader(
            loading = loading
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.Normal)
            ) {
                Text(
                    text = stringResource(id = R.string.title_register),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.subtitle_register),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.Normal)
                )

                TextFields.Default(
                    value = state.email,
                    onValueChange = { email ->
                        onEvent(RegisterUiEvent.EmailChanged(email))
                    },
                    placeholder = stringResource(id = R.string.email),
                    error = state.emailValidationResult.errorMessage(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Normal)
                )

                TextFields.Default(
                    value = state.password,
                    onValueChange = { password ->
                        onEvent(RegisterUiEvent.PasswordChanged(password))
                    },
                    placeholder = stringResource(id = R.string.password),
                    error = state.passwordValidationResult.errorMessage(),
                    obscureText = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Small)
                )

                TextFields.Default(
                    value = state.confirmPassword,
                    onValueChange = { confirmPassword ->
                        onEvent(RegisterUiEvent.ConfirmPasswordChanged(confirmPassword))
                    },
                    placeholder = stringResource(id = R.string.password_repeat),
                    error = state.confirmPasswordValidationResult.errorMessage(),
                    obscureText = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Small)
                )


                Buttons.Default(
                    text = stringResource(id = R.string.register),
                    enabled = isValid,
                    onClick = {
                        onEvent(RegisterUiEvent.OnRegisterClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Normal)
                )

                Buttons.Text(
                    text = stringResource(id = R.string.have_an_account),
                    onClick = {
                        onEvent(RegisterUiEvent.OnLoginClicked)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
