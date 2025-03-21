package com.tomtruyen.core.ui.toolbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.GlobalAppState
import com.tomtruyen.core.ui.R
import com.tomtruyen.navigation.shouldShowNavigationIcon

@Composable
fun Toolbar(
    title: String,
    navController: NavController,
    onNavigateUp: () -> Unit = { navController.popBackStack() },
    actions: @Composable RowScope.() -> Unit = {},
) {
    Toolbar(
        title = {
            ToolbarTitle(title = title)
        },
        navController = navController,
        onNavigateUp = onNavigateUp,
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: @Composable () -> Unit,
    navController: NavController,
    onNavigateUp: () -> Unit = { navController.popBackStack() },
    actions: @Composable RowScope.() -> Unit = {},
) {
    val isBottomBarVisible by GlobalAppState.isBottomBarVisible

    TopAppBar(
        title = title,
        navigationIcon = {
            if (navController.shouldShowNavigationIcon(isBottomBarVisible)) {
                IconButton(
                    onClick = {
                        onNavigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = stringResource(id = R.string.content_description_back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.typography.titleLarge.color,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        actions = actions,
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        )
    )
}

@Composable
fun ToolbarTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.W500,
    )
}