package com.tomtruyen.fitnessapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.navigation.MainBottomNavigation
import com.tomtruyen.core.designsystem.theme.FitnessApplicationTheme
import com.tomtruyen.feature.auth.login.LoginScreen
import com.tomtruyen.feature.auth.register.RegisterScreen
import com.tomtruyen.feature.exercises.ExercisesScreen
import com.tomtruyen.feature.exercises.ExercisesViewModel
import com.tomtruyen.feature.exercises.create.CreateExerciseScreen
import com.tomtruyen.feature.exercises.detail.ExerciseDetailScreen
import com.tomtruyen.feature.exercises.filter.ExercisesFilterScreen
import com.tomtruyen.feature.profile.ProfileScreen
import com.tomtruyen.feature.workouts.WorkoutsScreen
import com.tomtruyen.feature.workouts.manage.ManageWorkoutScreen
import com.tomtruyen.feature.workouts.manage.ManageWorkoutViewModel
import com.tomtruyen.feature.workouts.detail.WorkoutDetailScreen
import com.tomtruyen.feature.workouts.execute.ExecuteWorkoutScreen
import com.tomtruyen.feature.workouts.history.WorkoutHistoryScreen
import com.tomtruyen.models.Global
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.screenScopedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val userRepository by inject<UserRepository>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FitnessApplicationTheme {
                val navController = rememberNavController()

                var isBottomBarVisible by Global.isBottomBarVisible

                val backStackEntry by navController.currentBackStackEntryAsState()

                LaunchedEffect(Unit) {
                    if(userRepository.isLoggedIn()) {
                        navController.navigate(Screen.Workout.Graph) {
                            popUpTo(Screen.Auth.Graph) {
                                inclusive = true
                            }
                        }
                    }
                }

                LaunchedEffect(backStackEntry) {
                    val isRootDestination = listOf(
                        Screen.Workout.Overview,
                        Screen.Exercise.Overview(),
                        Screen.Workout.HistoryOverview,
                        Screen.Profile
                    ).any { backStackEntry?.destination?.hasRoute(it::class) ?: false }

                    val isViewMode = if(backStackEntry?.destination?.hasRoute<Screen.Exercise.Overview>() == true) {
                        backStackEntry?.toRoute<Screen.Exercise.Overview>()?.mode == Screen.Exercise.Overview.Mode.VIEW
                    } else true

                    isBottomBarVisible = isRootDestination && isViewMode
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
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Screen.Auth.Graph
                    ) {
                        navigation<Screen.Auth.Graph>(
                            startDestination = Screen.Auth.Login
                        ) {
                            composable<Screen.Auth.Login> {
                                LoginScreen(navController)
                            }

                            composable<Screen.Auth.Register> {
                                RegisterScreen(navController)
                            }
                        }

                        composable<Screen.Profile> {
                            ProfileScreen(navController)
                        }

                        navigation<Screen.Workout.Graph>(
                            startDestination = Screen.Workout.Overview
                        ) {
                            composable<Screen.Workout.Overview> {
                                WorkoutsScreen(navController)
                            }

                            composable<Screen.Workout.Detail> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Workout.Detail>()

                                WorkoutDetailScreen(
                                    id = args.id,
                                    navController = navController
                                )
                            }

                            composable<Screen.Workout.Create> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Workout.Create>()

                                val viewModel = koinViewModel<ManageWorkoutViewModel>(
                                    viewModelStoreOwner = backStackEntry,
                                    parameters = { parametersOf(args.id, args.execute) }
                                )

                                ManageWorkoutScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }

                            composable<Screen.Workout.Execute> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Workout.Execute>()

                                ExecuteWorkoutScreen(
                                    id = args.id,
                                    navController = navController
                                )
                            }

                            composable<Screen.Workout.HistoryOverview> {
                                WorkoutHistoryScreen(
                                    navController
                                )
                            }
                        }

                        navigation<Screen.Exercise.Graph>(
                            startDestination = Screen.Exercise.Overview()
                        ) {
                            composable<Screen.Exercise.Overview> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Exercise.Overview>()

                                val viewModel = screenScopedViewModel<Screen.Exercise.Overview, ExercisesViewModel>(
                                    navController = navController,
                                    backStackEntry = backStackEntry,
                                    parameters = { parametersOf(args.mode) }
                                )

                                ExercisesScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }

                            composable<Screen.Exercise.Detail> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Exercise.Detail>()

                                ExerciseDetailScreen(
                                    id = args.id,
                                    navController = navController
                                )
                            }

                            composable<Screen.Exercise.Create> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Exercise.Create>()

                                CreateExerciseScreen(
                                    id = args.id,
                                    navController = navController
                                )
                            }

                            composable<Screen.Exercise.Filter> { backStackEntry ->
                                val viewModel = screenScopedViewModel<Screen.Exercise.Overview, ExercisesViewModel>(
                                    navController = navController,
                                    backStackEntry = backStackEntry,
                                )

                                ExercisesFilterScreen(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}