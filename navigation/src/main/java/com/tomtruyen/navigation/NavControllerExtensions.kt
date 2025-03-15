package com.tomtruyen.navigation

import androidx.navigation.NavController
import com.tomtruyen.core.common.JsonInstance
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull

fun NavController.shouldShowNavigationIcon(isBottomBarVisible: Boolean): Boolean {
    return previousBackStackEntry != null
            && !isBottomBarVisible
}

inline fun <reified T: NavResult> NavController.setNavigationResult(
    result: T
) {
    previousBackStackEntry?.savedStateHandle?.set(
        key = result.key,
        value = JsonInstance.encodeToString<T>(result)
    )
}

suspend inline fun <reified T: NavResult> NavController.handleNavigationResult(
    key: String,
    crossinline onCollect: suspend (result: T) -> Unit
) {
    currentBackStackEntry?.savedStateHandle?.getStateFlow<String?>(key, null)
        ?.filterNotNull()
        ?.collectLatest { result ->
            onCollect(JsonInstance.decodeFromString<T>(result))

            // Clear NavResult
            currentBackStackEntry?.savedStateHandle?.remove<String?>(key)
        }
}