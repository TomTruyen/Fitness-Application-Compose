package com.tomtruyen.fitnessapplication.ui.shared.toolbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.extensions.shouldShowNavigationIcon
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbar(
    title: String,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val isBottomBarVisible by getKoin().get<GlobalProvider>().isBottomBarVisible

    LargeTopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            if(navController.shouldShowNavigationIcon(isBottomBarVisible)) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = stringResource(id = R.string.content_description_back)
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}