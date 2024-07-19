package com.tomtruyen.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TabLayout(
    items: List<String>,
    state: PagerState
)  {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = state.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 0.dp,
        divider = {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        }
    ) {
        items.forEachIndexed { index, item ->
            Tab(
                selected = state.currentPage == index,
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = item,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}