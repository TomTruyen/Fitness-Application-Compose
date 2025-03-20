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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens

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
        val focusManager = LocalFocusManager.current

        Button(
            onClick = {
                focusManager.clearFocus()
                onClick()
            },
            enabled = enabled,
            shape = shape,
            modifier = modifier.defaultMinSize(
                minHeight = minButtonSize,
            ),
            contentPadding = contentPadding,
            colors = colors,
        ) {
            Text(
                text = text,
                color = colors.contentColor
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
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        onClick: () -> Unit,
    ) {
        val focusManager = LocalFocusManager.current

        TextButton(
            onClick = {
                focusManager.clearFocus()
                onClick()
            },
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
                color = colors.contentColor,
                text = text
            )
        }
    }
}