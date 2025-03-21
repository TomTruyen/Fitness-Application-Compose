package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingContainer(
    loading: Boolean,
    scaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(scaffoldPadding)
            .fillMaxSize()
            .animateContentSize(),
    ) {
        content()

        Loader(
            loading = loading,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}