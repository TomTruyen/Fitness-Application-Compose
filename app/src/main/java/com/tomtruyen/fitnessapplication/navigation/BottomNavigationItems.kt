package com.tomtruyen.fitnessapplication.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.navigation.Screen

object BottomNavigation {
    val items = listOf(
        BottomNavigationItem(
            label = R.string.workouts,
            icon = Icons.Filled.Add,
            screen = Screen.Workout.Overview
        ),
        BottomNavigationItem(
            label = R.string.exercises,
            icon = Icons.Filled.FitnessCenter,
            screen = Screen.Exercise.Overview()
        ),
        BottomNavigationItem(
            label = R.string.history,
            icon = Icons.Filled.History,
            screen = Screen.Workout.HistoryOverview
        ),
        BottomNavigationItem(
            label = R.string.profile,
            icon = Icons.Filled.Person,
            screen = Screen.Profile
        ),
    )
}

data class BottomNavigationItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val screen: Screen,
)

