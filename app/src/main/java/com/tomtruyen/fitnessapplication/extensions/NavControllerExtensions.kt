package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController

fun NavController.shouldShowNavigationIcon(isBottomBarVisible: Boolean): Boolean {
    return previousBackStackEntry != null
            && !isBottomBarVisible
}