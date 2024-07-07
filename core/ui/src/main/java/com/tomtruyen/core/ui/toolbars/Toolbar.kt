package com.tomtruyen.core.ui.toolbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.theme.ChineseBlack
import com.tomtruyen.core.ui.R
import com.tomtruyen.models.Global
import com.tomtruyen.navigation.shouldShowNavigationIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String,
    navController: NavController,
    onNavigateUp: () -> Unit = { navController.popBackStack() },
    actions: @Composable RowScope.() -> Unit = {},
) {
    val isBottomBarVisible by Global.isBottomBarVisible

    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.W500,
                color = ChineseBlack
            )
        },
        navigationIcon = {
            if(navController.shouldShowNavigationIcon(isBottomBarVisible)) {
                IconButton(
                    onClick = {
                        onNavigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = stringResource(id = R.string.content_description_back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        actions = actions,
    )
}