package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BoxWithLoader(
    loading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        Loader(
            loading = loading,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}