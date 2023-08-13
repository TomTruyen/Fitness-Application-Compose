package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiEvent
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiState
import com.tomtruyen.fitnessapplication.ui.screens.destinations.LoginScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject

@RootNavGraph
@Destination
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is ProfileNavigationType.Logout -> {
                    navController.navigateAndClearBackStack(LoginScreenDestination)
                }
            }
        }
    }

    ProfileScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProfileScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Buttons.Default(
                text = stringResource(id = R.string.logout),
                onClick = { onEvent(ProfileUiEvent.Logout) }
            )
        }
    }
}