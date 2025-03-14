package com.tomtruyen.fitoryx

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tomtruyen.fitoryx.MainViewModel
import com.tomtruyen.core.common.models.GlobalAppState
import com.tomtruyen.core.designsystem.theme.FynixTheme
import com.tomtruyen.core.designsystem.theme.rememberDarkMode
import com.tomtruyen.feature.auth.login.LoginScreen
import com.tomtruyen.feature.auth.register.RegisterScreen
import com.tomtruyen.feature.exercises.ExercisesScreen
import com.tomtruyen.feature.exercises.ExercisesViewModel
import com.tomtruyen.feature.exercises.detail.ExerciseDetailScreen
import com.tomtruyen.feature.exercises.filter.ExercisesFilterScreen
import com.tomtruyen.feature.exercises.manage.ManageExerciseScreen
import com.tomtruyen.feature.profile.ProfileScreen
import com.tomtruyen.feature.workouts.WorkoutsScreen
import com.tomtruyen.feature.workouts.history.WorkoutHistoryScreen
import com.tomtruyen.feature.workouts.history.detail.WorkoutHistoryDetailScreen
import com.tomtruyen.feature.workouts.manage.ManageWorkoutScreen
import com.tomtruyen.feature.workouts.manage.ManageWorkoutViewModel
import com.tomtruyen.fitoryx.navigation.MainBottomNavigation
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.screenScopedViewModel
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !viewModel.hasCheckedLoggedIn.get() }

        setContent {
            KoinAndroidContext {
                val isDarkTheme by rememberDarkMode()

                LaunchedEffect(isDarkTheme) {
                    enableEdgeToEdge(
                        statusBarStyle = if(isDarkTheme) {
                            SystemBarStyle.dark(Color.TRANSPARENT)
                        } else {
                            SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                        }
                    )
                }

                FynixTheme {
                    val navController = rememberNavController()

                    var isBottomBarVisible by GlobalAppState.isBottomBarVisible

                    val backStackEntry by navController.currentBackStackEntryAsState()

                    LaunchedEffect(backStackEntry) {
                        val isRootDestination = listOf(
                            Screen.Workout.Overview,
                            Screen.Exercise.Overview(),
                            Screen.History.Overview,
                            Screen.Profile
                        ).any { backStackEntry?.destination?.hasRoute(it::class) == true }

                        val isViewMode =
                            if (backStackEntry?.destination?.hasRoute<Screen.Exercise.Overview>() == true) {
                                backStackEntry?.toRoute<Screen.Exercise.Overview>()?.mode == Screen.Exercise.Overview.Mode.VIEW
                            } else true

                        isBottomBarVisible = isRootDestination && isViewMode
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize(),
                        bottomBar = {
                            MainBottomNavigation(
                                navController = navController,
                                showBottomBar = isBottomBarVisible
                            )
                        }
                    ) {
                        SharedTransitionLayout {
                            NavHost(
                                modifier = Modifier
                                    .padding(it)
                                    .consumeWindowInsets(it),
                                navController = navController,
                                startDestination = Screen.Auth.Graph
                            ) {
                                navigation<Screen.Auth.Graph>(
                                    startDestination = Screen.Auth.Login
                                ) {
                                    composable<Screen.Auth.Login> {
                                        LaunchedEffect(Unit) {
                                            if(viewModel.isLoggedIn()) {
                                                navController.navigate(Screen.Workout.Graph) {
                                                    popUpTo(Screen.Auth.Graph) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        }

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
                                        WorkoutsScreen(
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }

                                    composable<Screen.Workout.Manage> { backStackEntry ->
                                        val args = backStackEntry.toRoute<Screen.Workout.Manage>()

                                        val viewModel = koinViewModel<ManageWorkoutViewModel>(
                                            viewModelStoreOwner = backStackEntry,
                                            parameters = { parametersOf(args.id, args.mode, args.workout) }
                                        )

                                        ManageWorkoutScreen(
                                            navController = navController,
                                            animatedVisibilityScope = this,
                                            viewModel = viewModel
                                        )
                                    }
                                }

                                navigation<Screen.Exercise.Graph>(
                                    startDestination = Screen.Exercise.Overview()
                                ) {
                                    composable<Screen.Exercise.Overview> { backStackEntry ->
                                        val args =
                                            backStackEntry.toRoute<Screen.Exercise.Overview>()

                                        val viewModel =
                                            screenScopedViewModel<Screen.Exercise.Overview, ExercisesViewModel>(
                                                navController = navController,
                                                backStackEntry = backStackEntry,
                                                parameters = { parametersOf(args.mode) }
                                            )

                                        ExercisesScreen(
                                            navController = navController,
                                            animatedVisibilityScope = this,
                                            viewModel = viewModel
                                        )
                                    }

                                    composable<Screen.Exercise.Detail> { backStackEntry ->
                                        val args = backStackEntry.toRoute<Screen.Exercise.Detail>()

                                        ExerciseDetailScreen(
                                            id = args.id,
                                            animatedVisibilityScope = this,
                                            navController = navController
                                        )
                                    }

                                    composable<Screen.Exercise.Manage> { backStackEntry ->
                                        val args = backStackEntry.toRoute<Screen.Exercise.Manage>()

                                        ManageExerciseScreen(
                                            id = args.id,
                                            navController = navController
                                        )
                                    }

                                    composable<Screen.Exercise.Filter> { backStackEntry ->
                                        val viewModel =
                                            screenScopedViewModel<Screen.Exercise.Overview, ExercisesViewModel>(
                                                navController = navController,
                                                backStackEntry = backStackEntry,
                                            )

                                        ExercisesFilterScreen(
                                            navController = navController,
                                            viewModel = viewModel
                                        )
                                    }
                                }

                                navigation<Screen.History.Graph>(
                                    startDestination = Screen.History.Overview
                                ) {
                                    composable<Screen.History.Overview> {
                                        WorkoutHistoryScreen(
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }

                                    composable<Screen.History.Detail> { backStackEntry ->
                                        val args = backStackEntry.toRoute<Screen.History.Detail>()

                                        WorkoutHistoryDetailScreen(
                                            id = args.id,
                                            navController = navController,
                                            animatedVisibilityScope = this
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}