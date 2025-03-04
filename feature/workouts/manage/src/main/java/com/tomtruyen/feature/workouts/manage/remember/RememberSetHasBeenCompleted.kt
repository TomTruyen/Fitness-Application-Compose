package com.tomtruyen.feature.workouts.manage.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberSetHasBeenCompleted(completed: Boolean): Boolean {
    var hasBeenCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(completed) {
        if (completed) hasBeenCompleted = true
    }

    return hasBeenCompleted
}