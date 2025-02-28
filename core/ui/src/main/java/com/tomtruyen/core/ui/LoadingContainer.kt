package com.tomtruyen.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomtruyen.core.designsystem.Dimens

@Composable
fun LoadingContainer(
    loading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier
            .weight(1f)
            .animateContentSize()) {
            content()
        }

        Loader(
            loading = loading,
            modifier = Modifier.padding(top = Dimens.Tiny)
        )
    }
}