package com.tomtruyen.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.tomtruyen.core.designsystem.Dimens
import kotlinx.coroutines.launch

data class BottomSheetItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val color: Color? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetList(
    items: List<BottomSheetItem>,
    visible: Boolean,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()

    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = state,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Normal)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),

                ) {
                items.forEach { item ->
                    BottomSheetListItem(
                        item = item,
                        onDismiss = {
                            scope.launch {
                                state.hide()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomSheetListItem(
    item: BottomSheetItem,
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
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = item.color ?: MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(id = item.title),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = item.color ?: MaterialTheme.colorScheme.onSurface
            )
        )
    }
}