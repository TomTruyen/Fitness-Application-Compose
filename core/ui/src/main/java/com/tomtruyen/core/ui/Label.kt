package com.tomtruyen.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Label(
    label: String,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        textAlign = textAlign,
    ),
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        style = style,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}