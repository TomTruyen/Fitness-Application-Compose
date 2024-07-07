package com.tomtruyen.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.tomtruyen.core.designsystem.theme.BlueGrey

@Composable
fun TextFieldPlaceholder(
    value: String?,
    placeholder: String,
    textStyle: TextStyle
) {
    if(value.isNullOrBlank()) {
        Text(
            text = placeholder,
            style = textStyle.copy(
                color = BlueGrey
            ),
            textAlign = textStyle.textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}