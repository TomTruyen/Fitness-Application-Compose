package com.tomtruyen.fitoryx.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tomtruyen.core.designsystem.Dimens

@Composable
fun MainBottomNavigation(
    navController: NavController,
    showBottomBar: Boolean,
) {
    val backstackEntry by navController.currentBackStackEntryAsState()

    val height by animateDpAsState(
        targetValue = if (showBottomBar) 96.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )

    NavigationBar(
        modifier = Modifier.height(height),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.Small),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigation.items.forEach { item ->
                val selected by remember(backstackEntry) {
                    mutableStateOf(
                        backstackEntry?.destination?.hasRoute(item.screen::class) == true
                    )
                }

                BottomBarItem(
                    navController = navController,
                    item = item,
                    selected = selected,
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    navController: NavController,
    item: BottomNavigationItem,
    selected: Boolean,
) {
    val animationSpec: AnimationSpec<Color> = tween(100, 0, LinearEasing)

    val animatedContentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = animationSpec,
        label = ""
    )

    val animatedContainerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.background
        },
        animationSpec = animationSpec,
        label = ""
    )

    val size = 56.dp

    Card(
        modifier = Modifier.size(
            width = size,
            height = size,
        ),
        shape = FloatingActionButtonDefaults.shape,
        onClick = {
            if (selected) return@Card

            navController.navigate(item.screen) {
                launchSingleTop = true
            }
        },
        colors = CardDefaults.cardColors(
            contentColor = animatedContentColor,
            containerColor = animatedContainerColor,
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(id = item.label),
                modifier = if (item.icon == Icons.Rounded.FitnessCenter) {
                    Modifier.rotate(-45f)
                } else {
                    Modifier
                }.align(Alignment.Center)
            )
        }
    }
}