package com.tomtruyen.fitnessapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.currentDestinationFlow
import com.tomtruyen.fitnessapplication.extensions.navigateAndClearBackStack
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.navigation.MainBottomNavigation
import com.tomtruyen.fitnessapplication.navigation.NavArguments
import com.tomtruyen.fitnessapplication.ui.screens.NavGraphs
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ProfileScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutHistoryScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.theme.FitnessApplicationTheme
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    private val userRepository by inject<UserRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessApplicationTheme {
                val navController = rememberNavController()
                val navHostEngine = rememberAnimatedNavHostEngine(
                    navHostContentAlignment = Alignment.TopCenter,
                    rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
                )

                var isBottomBarVisible by getKoin().get<GlobalProvider>().isBottomBarVisible

                val destination by navController.currentDestinationFlow.collectAsStateWithLifecycle(
                    initialValue = navController.currentDestinationAsState()
                )

                LaunchedEffect(Unit) {
                    if(userRepository.isLoggedIn()) {
                        navController.navigateAndClearBackStack(
                            destination = WorkoutOverviewScreenDestination
                        )
                    }
                }

                LaunchedEffect(destination) {
                    val isRootDestinations = destination in listOf(
                        WorkoutOverviewScreenDestination,
                        ExercisesScreenDestination,
                        WorkoutHistoryScreenDestination,
                        ProfileScreenDestination,
                    )

                    val isExercisesFromWorkout = if(destination is ExercisesScreenDestination) {
                        // Get the isFromWorkout argument from the destination
                        navController.getBackStackEntry((destination as ExercisesScreenDestination).route)
                            .let {
                                it.arguments?.getBoolean(NavArguments.IS_FROM_WORKOUT, false) ?: false
                            }
                    } else {
                        false
                    }

                    isBottomBarVisible = isRootDestinations && !isExercisesFromWorkout
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MainBottomNavigation(
                            navController = navController,
                            showBottomBar = isBottomBarVisible
                        )
                    }
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        engine = navHostEngine,
                        navController = navController,
                        dependenciesContainerBuilder = {
                            dependency(navController)

                            // Required to have a SharedViewModel
                            dependency(NavGraphs.exercises) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.getBackStackEntry(NavGraphs.exercises.route)
                                }

                                koinViewModel<ExercisesViewModel>(viewModelStoreOwner = parentEntry) {
                                    parametersOf(
                                        parentEntry.arguments?.getBoolean(NavArguments.IS_FROM_WORKOUT, false) ?: false
                                    )
                                }
                            }

                            dependency(NavGraphs.createWorkout) {
                                val parentEntry = remember(navBackStackEntry) {
                                    navController.getBackStackEntry(NavGraphs.createWorkout.route)
                                }

                                koinViewModel<CreateWorkoutViewModel>(viewModelStoreOwner = parentEntry) {
                                    parametersOf(
                                        parentEntry.arguments?.getString(NavArguments.ID, null)
                                    )
                                }
                            }
                        },
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
