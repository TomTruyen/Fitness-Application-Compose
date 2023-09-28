package com.tomtruyen.fitnessapplication.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutOverviewScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ProfileScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutHistoryScreenDestination

object BottomNavigation {
    val items = listOf(
        BottomNavigationItem(
            label = R.string.workouts,
            icon = Icons.Filled.Add,
            route = WorkoutOverviewScreenDestination.route
        ),
        BottomNavigationItem(
            label = R.string.exercises,
            icon = Icons.Filled.FitnessCenter,
            route = ExercisesScreenDestination.route
        ),
        BottomNavigationItem(
            label = R.string.history,
            icon = Icons.Filled.History,
            route = WorkoutHistoryScreenDestination.route
        ),
        BottomNavigationItem(
            label = R.string.profile,
            icon = Icons.Filled.Person,
            route = ProfileScreenDestination.route
        ),
    )
}
data class BottomNavigationItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val route: String,
)

