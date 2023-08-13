package com.tomtruyen.fitnessapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainBottomNavigation(
    navController: NavController,
    showBottomBar: Boolean,
) {
    val backstackEntry = navController.currentBackStackEntryAsState()

    if(showBottomBar) {
        NavigationBar {
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
                            modifier = if(item.icon == Icons.Filled.FitnessCenter) {
                                Modifier.rotate(-45f)
                            } else {
                                Modifier
                            }
                        )
                    }
                )
            }
        }
    }
}