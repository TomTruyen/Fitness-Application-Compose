package com.tomtruyen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition

/*
 Shared ViewModel Scoped to a [Screen]

 Usage: create an instance with the required parameters and the ViewModel will be scoped to the [Screen]
        If you want to use it in a nested screen, then just use [screenScopedViewModel] with the parent screen as [ScopedScreen]
        Leave parameters as null if the ViewModel doesn't require any parameters since it will get it from the parent screen
*/
@Composable
inline fun <reified ScopedScreen : Screen, reified VM : ViewModel> screenScopedViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    noinline parameters: ParametersDefinition? = null,
): VM {
    val parentEntry: NavBackStackEntry = remember(navController, backStackEntry) {
        navController.getBackStackEntry<ScopedScreen>()
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry,
        parameters = parameters,
    )
}