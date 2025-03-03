package com.tomtruyen.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import kotlinx.coroutines.launch

data class BottomSheetItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
    val color: Color? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetList(
    items: List<BottomSheetItem>,
    visible: Boolean,
    selectedIndex: Int? = null,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()
    val listState = rememberLazyListState()

    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Normal)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                ) {
                    itemsIndexed(items) { index, item ->
                        Column {
                            BottomSheetListItem(
                                item = item,
                                selected = index == selectedIndex,
                                onDismiss = {
                                    scope.launch {
                                        state.hide()
                                    }.invokeOnCompletion {
                                        onDismiss()
                                    }
                                }
                            )

                            if(index < items.lastIndex) {
                                HorizontalDivider()
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun BottomSheetListItem(
    item: BottomSheetItem,
    selected: Boolean,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                item.onClick()
                onDismiss()
            }
            .padding(Dimens.Normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Normal, Alignment.Start)
    ) {
        item.icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = item.color ?: MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = item.titleRes?.let { stringResource(id = item.titleRes) } ?: item.title.orEmpty(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = item.color ?: MaterialTheme.colorScheme.onSurface
            )
        )

        if(selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = item.color ?: MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}