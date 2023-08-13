package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.Direction

fun NavController.navigateAndClearBackStack(
    destination: Direction,
) {
    val currentDestination = currentDestination?.route

    navigate(destination.route) {
        currentDestination?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
    }
}