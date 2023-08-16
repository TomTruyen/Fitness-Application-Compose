package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.BuildConfig
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.ui.screens.destinations.LoginScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.CollapsingToolbar
import com.tomtruyen.fitnessapplication.ui.shared.ListItem
import com.tomtruyen.fitnessapplication.ui.shared.SwitchListItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

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
                    navController.navigateAndClearBackStack(
                        destination = LoginScreenDestination,
                        popUpTo = WorkoutOverviewScreenDestination
                    )
                }
            }
        }
    }

    ProfileScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CollapsingToolbar(
                title = stringResource(id = R.string.profile),
                navController =  navController,
                scrollBehavior = scrollBehavior
            )
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.units),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier
                    .padding(horizontal = Dimens.Normal)
                    .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
            )

            ListItem(
                title = stringResource(id = R.string.weight_unit),
                message = "kg",
            ) {
                // TODO: Setup dialog to display + make message value dynamic
            }

            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            )

            Text(
                text = stringResource(id = R.string.rest_timer),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier
                    .padding(horizontal = Dimens.Normal)
                    .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
            )

            ListItem(
                title = stringResource(id = R.string.default_rest_timer),
                message = "30s",
            ) {
                // TODO: Setup dialog to display + make message value dynamic
            }

            SwitchListItem(
                title = stringResource(id = R.string.rest_timer_enabled),
                checked = true
            ) {
                // TODO: update value
            }

            SwitchListItem(
                title = stringResource(id = R.string.vibrate_upon_finish),
                checked = true
            ) {
                // TODO: update value
            }

            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            )

            Text(
                text = stringResource(id = R.string.contact_and_support),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier
                    .padding(horizontal = Dimens.Normal)
                    .padding(top = Dimens.Normal, bottom = Dimens.Tiny)
            )

            ListItem(
                title = stringResource(id = R.string.report_an_issue),
                message = stringResource(id = R.string.message_report_an_issue),
            ) {
                // TODO: Setup Email client with user information prefilled + email subject and recipient
            }

            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            )

            Spacer(modifier = Modifier.weight(1f))

            Buttons.Default(
                text = stringResource(id = R.string.logout),
                onClick = { onEvent(ProfileUiEvent.Logout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Normal)
            )

            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            )

            Text(
                text = stringResource(id = R.string.version_and_build, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = Dimens.Normal, top = Dimens.Small)
                    .padding(horizontal = Dimens.Normal)
            )
        }
    }
}