package com.tomtruyen.core.common.controller

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

object SnackbarController {
    private val _events = Channel<SnackbarMessage>()
    val events = _events.receiveAsFlow()

    fun showSnackbar(snackbarMessage: SnackbarMessage) {
        _events.trySend(snackbarMessage)
    }

    @Composable
    fun GlobalSnackbarHost(colors: SnackbarColors) {
        val lifecycleOwner = LocalLifecycleOwner.current

        val snackbarHostState = remember {
            SnackbarHostState()
        }

        var snackbarMessage: SnackbarMessage by remember { mutableStateOf(SnackbarMessage.Empty) }

        LaunchedEffect(lifecycleOwner.lifecycle, events) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    events.collect { event ->
                        if (event !is SnackbarMessage.Empty && !event.message.isNullOrBlank()) {
                            snackbarHostState.currentSnackbarData?.dismiss()

                            snackbarMessage = event

                            snackbarHostState.showSnackbar(event.message.orEmpty())
                        }
                    }
                }
            }
        }

        val colorScheme by remember {
            derivedStateOf {
                when(snackbarMessage) {
                    SnackbarMessage.Empty -> Color.Transparent to Color.Transparent // We don't show this anyways so who cares
                    is SnackbarMessage.Error -> colors.error to colors.onError
                    is SnackbarMessage.Success -> colors.success to colors.onSuccess
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Snackbar(
                containerColor = colorScheme.first,
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (snackbarMessage.icon != null) {
                        Icon(
                            imageVector = snackbarMessage.icon!!,
                            contentDescription = null,
                            tint = colorScheme.second
                        )
                    }

                    Text(
                        text = snackbarMessage.message.orEmpty(),
                        color = colorScheme.second,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}

interface SnackbarColors {
    val error: Color
    val onError: Color
    val success: Color
    val onSuccess: Color
}

sealed class SnackbarMessage(
    open val message: String? = null,
    open val icon: ImageVector? = null,
) {
    data object Empty : SnackbarMessage()
    class Success(
        override val message: String,
        override val icon: ImageVector? = Icons.Rounded.Check,
    ) : SnackbarMessage(message, icon)

    class Error(
        override val message: String,
        override val icon: ImageVector? = Icons.Rounded.ErrorOutline,
    ) : SnackbarMessage(message, icon)
}