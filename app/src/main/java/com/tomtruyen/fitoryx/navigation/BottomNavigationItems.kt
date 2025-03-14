package com.tomtruyen.fitoryx.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.tomtruyen.fitoryx.R
import com.tomtruyen.navigation.Screen

object BottomNavigation {
    val items = listOf(
        BottomNavigationItem(
            label = R.string.workouts,
            icon = Icons.Rounded.Add,
            screen = Screen.Workout.Overview
        ),
        BottomNavigationItem(
            label = R.string.exercises,
            icon = Icons.Rounded.FitnessCenter,
            screen = Screen.Exercise.Overview()
        ),
        BottomNavigationItem(
            label = R.string.history,
            icon = Icons.Rounded.History,
            screen = Screen.History.Overview
        ),
        BottomNavigationItem(
            label = R.string.profile,
            icon = Icons.Rounded.Person,
            screen = Screen.Profile
        ),
    )
}

data class BottomNavigationItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val screen: Screen,
)

