package com.tomtruyen.fitnessapplication.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tomtruyen.fitnessapplication.Dimens

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
                .padding(top = Dimens.Tiny)
        )
    }
}