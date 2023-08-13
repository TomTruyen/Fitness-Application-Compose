package com.tomtruyen.fitnessapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.currentDestinationFlow
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.navigation.MainBottomNavigation
import com.tomtruyen.fitnessapplication.ui.screens.NavGraphs
import com.tomtruyen.fitnessapplication.ui.screens.destinations.LoginScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.RegisterScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.theme.FitnessApplicationTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val userRepository by inject<UserRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessApplicationTheme {
                val navController = rememberNavController()
                val destination by navController.currentDestinationFlow.collectAsStateWithLifecycle(
                    initialValue = navController.currentDestinationAsState()
                )

                var showBottomBar by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit) {
                    if(userRepository.isLoggedIn()) {
                        navController.navigateAndClearBackStack(WorkoutOverviewScreenDestination)
                    }
                }

                LaunchedEffect(destination) {
                    showBottomBar = destination !in listOf(
                        LoginScreenDestination,
                        RegisterScreenDestination
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MainBottomNavigation(
                            navController = navController,
                            showBottomBar = showBottomBar
                        )
                    }
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        dependenciesContainerBuilder = {
                            dependency(navController)
                        },
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
