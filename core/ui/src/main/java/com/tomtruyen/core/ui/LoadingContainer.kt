package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.models.GlobalAppState

@Composable
fun LoadingContainer(
    loading: Boolean,
    scaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isBottomBarVisible by GlobalAppState.isBottomBarVisible

    Box(
        modifier = modifier
            .padding(
                top = scaffoldPadding.calculateTopPadding(),
                start = scaffoldPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = scaffoldPadding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = if(isBottomBarVisible) 0.dp else scaffoldPadding.calculateBottomPadding()
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