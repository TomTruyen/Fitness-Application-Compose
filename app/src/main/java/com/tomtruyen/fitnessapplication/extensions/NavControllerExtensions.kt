package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.Direction
import com.tomtruyen.fitnessapplication.navigation.BottomNavigation

fun NavController.navigateAndClearBackStack(
    destination: Direction,
    popUpTo: Direction,
) {
    navigate(destination.route) {
        popUpTo(popUpTo.route) {
            inclusive = true
        }
    }
}

fun NavController.shouldShowNavigationIcon(isBottomBarVisible: Boolean): Boolean {
    return previousBackStackEntry != null
            && !isBottomBarVisible
}