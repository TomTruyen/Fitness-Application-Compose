package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.Direction

fun NavController.navigateAndClearBackStack(destination: Direction) {
    navigate(destination.route) {
        popUpTo(this@navigateAndClearBackStack.graph.id) {
            inclusive = true
        }
    }
}

fun NavController.shouldShowNavigationIcon(isBottomBarVisible: Boolean): Boolean {
    return previousBackStackEntry != null
            && !isBottomBarVisible
}