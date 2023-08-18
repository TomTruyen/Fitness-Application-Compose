package com.tomtruyen.fitnessapplication.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainBottomNavigation(
    navController: NavController,
    showBottomBar: Boolean,
) {
    val backstackEntry = navController.currentBackStackEntryAsState()

    AnimatedVisibility(
        visible = showBottomBar,
        enter = fadeIn() + expandIn(expandFrom = Alignment.BottomCenter),
        exit = shrinkOut(shrinkTowards = Alignment.BottomCenter) + fadeOut()
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            BottomNavigation.items.forEach { item ->
                val selected = item.route == backstackEntry.value?.destination?.route

                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    label = {
                        Text(
                            text = stringResource(id = item.label),
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(id = item.label),
                            modifier = if (item.icon == Icons.Filled.FitnessCenter) {
                                Modifier.rotate(-45f)
                            } else {
                                Modifier
                            }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.tertiary,
                        selectedIconColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
            }
        }
    }
}