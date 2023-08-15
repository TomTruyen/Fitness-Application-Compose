package com.tomtruyen.fitnessapplication.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomtruyen.fitnessapplication.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel<TNavigationType>: ViewModel() {
    protected val vmScope = viewModelScope

    val loading = MutableStateFlow(false)

    private val navigationChannel = Channel<TNavigationType>()
    val navigation = navigationChannel.receiveAsFlow()

    private var snackbarMessage by mutableStateOf<SnackbarMessage>(SnackbarMessage.Empty)

    protected fun isLoading(loading: Boolean) = this.loading.tryEmit(loading)

    protected fun launchLoading(block: suspend CoroutineScope.() -> Unit) = vmScope.launch(Dispatchers.IO) {
        isLoading(true)
        block()
        isLoading(false)
    }

    protected fun launchIO(block: suspend CoroutineScope.() -> Unit) = vmScope.launch(Dispatchers.IO) {
        block()
    }

    protected fun navigate(navigationType: TNavigationType) = vmScope.launch {
        navigationChannel.send(navigationType)
    }

    @Composable
    fun CreateSnackbarHost() {
        val snackbarHostState = remember {
            SnackbarHostState()
        }

        LaunchedEffect(snackbarMessage) {
            if(snackbarMessage !is SnackbarMessage.Empty && !snackbarMessage.message.isNullOrBlank()) {
                snackbarHostState.showSnackbar(snackbarMessage.message!!)
                snackbarMessage = SnackbarMessage.Empty
            }
        }

        SnackbarHost(
            hostState = snackbarHostState
        ) {
            Snackbar(
                containerColor = snackbarMessage.backgroundColor ?: MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(Dimens.Small),
                shape = RoundedCornerShape(Dimens.Small)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(snackbarMessage.icon != null) {
                        Icon(
                            imageVector = snackbarMessage.icon!!,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Text(
                        text = snackbarMessage.message ?: "",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = Dimens.Small)
                            .weight(1f)
                    )
                }
            }
        }
    }

    fun showSnackbar(snackbarMessage: SnackbarMessage) {
        this.snackbarMessage = snackbarMessage
    }
}