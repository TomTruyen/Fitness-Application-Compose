package com.tomtruyen.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.tomtruyen.core.designsystem.theme.placeholder

@Composable
fun TextFieldPlaceholder(
    value: String?,
    placeholder: String,
    textStyle: TextStyle
) {
    AnimatedVisibility(
        visible = value.isNullOrBlank(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(
            text = placeholder,
            style = textStyle.copy(
                color = MaterialTheme.colorScheme.placeholder
            ),
            textAlign = textStyle.textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}