package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.abs
@Composable
fun RestTimePicker(
    modifier: Modifier = Modifier,
    value: Int,
    leadingZero: Boolean = true,
    minutesRange: Iterable<Int> = (0..59),
    secondsRange: Iterable<Int> = (0..59),
    minutesDivider: (@Composable () -> Unit)? = null,
    secondsDivider: (@Composable () -> Unit)? = null,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val minutes = value / 60
    val seconds = value % 60

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberPicker(
            modifier = Modifier.weight(1f)
                .animateContentSize(),
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            value = minutes,
            onValueChange = {
                val totalSeconds = it * 60 + seconds
                onValueChange(totalSeconds)
            },
            dividersColor = dividersColor,
            textStyle = textStyle,
            list = minutesRange.toList()
        )

        minutesDivider?.invoke()

        Text(
            text = ":",
            style = textStyle.copy(
                fontWeight = FontWeight.W500,
                fontSize = textStyle.fontSize.times(1.5f)
            ),
        )

        NumberPicker(
            modifier = Modifier.weight(1f)
                .animateContentSize(),
            label = {
                "${if (leadingZero && abs(it) < 10) "0" else ""}$it"
            },
            value = seconds,
            onValueChange = {
                val totalSeconds = minutes * 60 + it
                onValueChange(totalSeconds)
            },
            dividersColor = dividersColor,
            textStyle = textStyle,
            list = secondsRange.toList()
        )

        secondsDivider?.invoke()
    }
}