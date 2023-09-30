package com.tomtruyen.fitnessapplication.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.ui.theme.ChineseBlack
import com.tomtruyen.fitnessapplication.ui.theme.ChineseSilver

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
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = Dimens.Small),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavigation.items.forEach { item ->
                    val selected = item.route == backstackEntry.value?.destination?.route
                    BottomBarItem(
                        navController = navController,
                        item = item,
                        selected = selected,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarItem(
    navController: NavController,
    item: BottomNavigationItem,
    selected: Boolean,
) {
    val animationSpec: AnimationSpec<Color> = tween(100, 0, LinearEasing)

    val animatedContentColor by animateColorAsState(
        targetValue = if (selected) Color.White else ChineseSilver,
        animationSpec = animationSpec,
        label = ""
    )

    val animatedContainerColor by animateColorAsState(
        targetValue = if (selected) ChineseBlack else Color.Transparent,
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
            navController.navigate(item.route)
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
                modifier = if (item.icon == Icons.Filled.FitnessCenter) {
                    Modifier.rotate(-45f)
                } else {
                    Modifier
                }.align(Alignment.Center)
            )
        }
    }
}