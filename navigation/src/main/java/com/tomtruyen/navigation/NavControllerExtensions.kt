package com.tomtruyen.navigation

import androidx.navigation.NavController

fun NavController.shouldShowNavigationIcon(isBottomBarVisible: Boolean): Boolean {
    return previousBackStackEntry != null
            && !isBottomBarVisible
}