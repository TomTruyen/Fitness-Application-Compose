package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun LoadingContainer(
    loading: Boolean,
    scaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    Box(
        modifier = modifier
            .padding(
                top = scaffoldPadding.calculateTopPadding(),
                start = scaffoldPadding.calculateStartPadding(layoutDirection),
                end = scaffoldPadding.calculateEndPadding(layoutDirection),
                bottom = 0.dp
            )
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