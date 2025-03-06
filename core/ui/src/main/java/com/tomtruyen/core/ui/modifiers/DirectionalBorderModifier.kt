package com.tomtruyen.core.ui.modifiers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class BorderSide {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;
}

@Composable
fun Modifier.directionalBorder(
    width: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    sides: List<BorderSide>
) = sides.fold(this) { acc, side ->
    acc.directionalBorder(width, color, side)
}

@Composable
fun Modifier.directionalBorder(
    width: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    side: BorderSide
) = drawBehind {
    val strokeWidth = width.value * density
    val strokeCenter = strokeWidth / 2

    when (side) {
        BorderSide.TOP -> drawLine(
            color = color,
            start = Offset(0f, strokeCenter),
            end = Offset(size.width, strokeCenter),
            strokeWidth = strokeWidth
        )
        BorderSide.BOTTOM -> drawLine(
            color = color,
            start = Offset(0f, size.height - strokeCenter),
            end = Offset(size.width, size.height - strokeCenter),
            strokeWidth = strokeWidth
        )
        BorderSide.LEFT -> drawLine(
            color = color,
            start = Offset(strokeCenter, 0f),
            end = Offset(strokeCenter, size.height),
            strokeWidth = strokeWidth
        )
        BorderSide.RIGHT -> drawLine(
            color = color,
            start = Offset(size.width - strokeCenter, 0f),
            end = Offset(size.width - strokeCenter, size.height),
            strokeWidth = strokeWidth
        )
    }
}