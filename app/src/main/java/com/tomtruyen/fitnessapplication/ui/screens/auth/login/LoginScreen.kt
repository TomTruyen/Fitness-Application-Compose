package com.tomtruyen.fitnessapplication.ui.screens.auth.login

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.ui.screens.destinations.RegisterScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.SocialButtons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.validation.errorMessage
import com.tomtruyen.fitnessapplication.validation.isValid
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
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

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is LoginNavigationType.Home -> {
                    navController.navigateAndClearBackStack(
                        destination = WorkoutOverviewScreenDestination
                    )
                }
                is LoginNavigationType.Register -> {
                    navController.navigateAndClearBackStack(
                        destination = RegisterScreenDestination
                    )
                }
            }
        }
    }

    LoginScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        state = state,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LoginScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: LoginUiState,
    loading: Boolean,
    onEvent: (LoginUiEvent) -> Unit
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
            loading = loading,
            modifier = Modifier.padding(it)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.Normal)
            ) {
                Text(
                    text = stringResource(id = R.string.title_login),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.subtitle_login),
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
                        onEvent(LoginUiEvent.EmailChanged(email))
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
                        onEvent(LoginUiEvent.PasswordChanged(password))
                    },
                    placeholder = stringResource(id = R.string.password),
                    error = state.passwordValidationResult.errorMessage(),
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
                    text = stringResource(id = R.string.login),
                    enabled = isValid && !loading,
                    onClick = {
                        focusManager.clearFocus()
                        onEvent(LoginUiEvent.OnLoginClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.Normal)
                )

                SocialButtons.Google(
                    text = stringResource(id = R.string.button_login_google),
                    enabled = !loading,
                    onClick = focusManager::clearFocus,
                    onSuccess = { idToken ->
                        onEvent(LoginUiEvent.OnGoogleSignInSuccess(idToken))
                    },
                    onError = { error ->
                        onEvent(LoginUiEvent.OnGoogleSignInFailed(error))
                    }
                )

                Buttons.Text(
                    text = stringResource(id = R.string.need_account),
                    enabled = !loading,
                    onClick = {
                        onEvent(LoginUiEvent.OnRegisterClicked)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}