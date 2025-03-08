package com.tomtruyen.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.textButtonContentColor

object Buttons {
    @Composable
    fun Default(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.small,
        minButtonSize: Dp = Dimens.MinButtonHeight,
        colors: ButtonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        ),
        contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        onClick: () -> Unit,
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            modifier = modifier.defaultMinSize(
                minHeight = minButtonSize,
            ),
            contentPadding = contentPadding,
            colors = colors,
        ) {
            Text(
                text = text
            )
        }
    }

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        icon: ImageVector? = null,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.small,
        colors: ButtonColors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick: () -> Unit,
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = colors,
            modifier = modifier.defaultMinSize(
                minHeight = Dimens.MinButtonHeight,
            ),
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                modifier = Modifier.padding(vertical = Dimens.Tiny),
                text = text
            )
        }
    }

    @Composable
    fun Icon(
        icon: ImageVector,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.small,
        colors: ButtonColors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        onClick: () -> Unit,
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = colors,
            modifier = modifier.defaultMinSize(
                minWidth = Dimens.MinButtonHeight,
                minHeight = Dimens.MinButtonHeight,
            ),
        ) {
            Icon(
                modifier = Modifier
                    .padding(vertical = Dimens.Tiny),
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}