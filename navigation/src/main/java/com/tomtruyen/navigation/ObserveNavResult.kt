package com.tomtruyen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController

@Composable
inline fun <reified T : NavResult> ObserveNavResult(
    navController: NavController,
    key: String,
    crossinline onResult: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(navController, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navController.handleNavigationResult<T>(
                key = key,
                onCollect = onResult
            )
        }
    }
}