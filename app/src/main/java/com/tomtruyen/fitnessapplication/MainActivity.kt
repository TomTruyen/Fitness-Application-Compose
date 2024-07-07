package com.tomtruyen.fitnessapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.ExercisesViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create.CreateExerciseScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail.ExerciseDetailScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.exercises.filter.ExercisesFilterScreen
import com.tomtruyen.feature.profile.ProfileScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.WorkoutOverviewScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder.ReorderWorkoutExercisesScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail.WorkoutDetailScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute.ExecuteWorkoutScreen
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history.WorkoutHistoryScreen
import com.tomtruyen.models.Global
import com.tomtruyen.navigation.Screen
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val userRepository by inject<UserRepository>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        Screen.Exercise.Overview(false),
                        Screen.Workout.HistoryOverview,
                        Screen.Profile
                    ).any { backStackEntry?.destination?.hasRoute(it::class) ?: false }

                    val isExercisesFromWorkout = if(backStackEntry?.destination?.hasRoute<Screen.Exercise.Overview>() == true) {
                        backStackEntry?.toRoute<Screen.Exercise.Overview>()?.isFromWorkout ?: false
                    } else false

                    isBottomBarVisible = isRootDestination && !isExercisesFromWorkout
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
                            com.tomtruyen.feature.profile.ProfileScreen(navController)
                        }

                        navigation<Screen.Workout.Graph>(
                            startDestination = Screen.Workout.Overview
                        ) {
                            composable<Screen.Workout.Overview> {
                                WorkoutOverviewScreen(navController)
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

                                val parentEntry =
                                    navController.getBackStackEntry<Screen.Workout.Graph>()

                                val viewModel = koinNavViewModel<CreateWorkoutViewModel>(
                                    viewModelStoreOwner = parentEntry,
                                    parameters = { parametersOf(args.id) }
                                )

                                CreateWorkoutScreen(
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
                                WorkoutHistoryScreen(navController)
                            }

                            composable<Screen.Workout.ReorderExercises> {
                                val parentEntry =
                                    navController.getBackStackEntry<Screen.Workout.Graph>()

                                val viewModel = koinNavViewModel<CreateWorkoutViewModel>(
                                    viewModelStoreOwner = parentEntry
                                )

                                ReorderWorkoutExercisesScreen(
                                    navController = navController,
                                    parentViewModel = viewModel
                                )
                            }
                        }

                        navigation<Screen.Exercise.Graph>(
                            startDestination = Screen.Exercise.Overview(false)
                        ) {
                            composable<Screen.Exercise.Overview> { backStackEntry ->
                                val args = backStackEntry.toRoute<Screen.Exercise.Overview>()

                                val parentEntry =
                                    navController.getBackStackEntry<Screen.Exercise.Graph>()

                                val viewModel = koinNavViewModel<ExercisesViewModel>(
                                    viewModelStoreOwner = parentEntry,
                                    parameters = { parametersOf(args.isFromWorkout) }
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

                            composable<Screen.Exercise.Filter> {
                                val parentEntry =
                                    navController.getBackStackEntry<Screen.Exercise.Graph>()

                                val viewModel = koinNavViewModel<ExercisesViewModel>(
                                    viewModelStoreOwner = parentEntry,
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
