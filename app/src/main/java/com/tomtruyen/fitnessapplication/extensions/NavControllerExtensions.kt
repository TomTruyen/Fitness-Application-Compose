package com.tomtruyen.fitnessapplication.extensions

import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.Direction

fun NavController.navigateAndClearBackStack(
    destination: Direction,
    popUntilDestination: Direction,
    inclusive: Boolean = true
) {
    navigate(destination.route) {
        popUpTo(popUntilDestination.route) {
            this.inclusive = inclusive
        }
    }
}