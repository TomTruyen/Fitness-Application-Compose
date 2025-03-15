package com.tomtruyen.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomtruyen.core.common.controller.SnackbarController
import com.tomtruyen.core.common.controller.SnackbarMessage
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel<UIState, UIAction, UIEvent>(
    initialState: UIState
) : ViewModel() {
    protected val vmScope = viewModelScope + Dispatchers.IO

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        val message = when (throwable) {
            is AuthRestException -> throwable.errorDescription
            is RestException -> throwable.error
            else -> throwable.localizedMessage ?: throwable.message
        }

        SnackbarController.showSnackbar(SnackbarMessage.Error(message.orEmpty()))

        isLoading(false)
        isRefreshing(false)
    }

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<UIEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    protected fun isLoading(loading: Boolean) {
        _loading.update { loading }
    }

    protected fun isRefreshing(refreshing: Boolean) {
        _refreshing.update { refreshing }
    }

    protected fun launch(block: suspend () -> Unit) = vmScope.launch(exceptionHandler) {
        block()
    }

    protected fun launchLoading(refresh: Boolean = false, block: suspend () -> Unit) =
        vmScope.launch(exceptionHandler) {
            if (refresh) isRefreshing(true) else isLoading(true)
            block()
            if (refresh) isRefreshing(false) else isLoading(false)
        }

    protected fun updateState(block: (UIState) -> UIState) {
        _uiState.update(block)
    }

    protected fun updateAndGetState(block: (UIState) -> UIState): UIState {
        return _uiState.updateAndGet(block)
    }

    protected fun triggerEvent(event: UIEvent) = vmScope.launch {
        _eventChannel.trySend(event)
    }

    abstract fun onAction(action: UIAction)
}