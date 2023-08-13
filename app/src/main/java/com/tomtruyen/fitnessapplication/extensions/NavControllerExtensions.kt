package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.Direction

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
