package com.tomtruyen.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loader(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LinearProgressIndicator(
            modifier = modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}