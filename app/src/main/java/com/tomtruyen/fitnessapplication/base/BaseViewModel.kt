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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomtruyen.fitnessapplication.Dimens
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UIState, UIAction, UIEvent>(
    initialState: UIState
): ViewModel() {
    protected val vmScope = viewModelScope

    private var snackbarMessage by mutableStateOf<SnackbarMessage>(SnackbarMessage.Empty)

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<UIEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    protected fun isLoading(loading: Boolean) {
        _loading.update { loading }
    }

    protected fun launchLoading(block: suspend () -> Unit) = vmScope.launch {
        isLoading(true)
        block()
        isLoading(false)
    }

    protected fun updateState(block: (UIState) -> UIState) {
        _uiState.update(block)
    }

    protected fun triggerEvent(event: UIEvent) = vmScope.launch {
        _eventChannel.trySend(event)
    }

    abstract fun onAction(action: UIAction)

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
                        text = snackbarMessage.message.orEmpty(),
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