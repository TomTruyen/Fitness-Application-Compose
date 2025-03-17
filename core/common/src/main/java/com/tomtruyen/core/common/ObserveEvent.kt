package com.tomtruyen.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.tomtruyen.core.common.base.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <VM : BaseViewModel<*, *, Event>, Event> ObserveEvent(
    viewModel: VM,
    onEvent: suspend (Event) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.eventFlow.collectLatest(onEvent)
        }
    }
}
