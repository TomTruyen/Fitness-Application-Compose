package com.tomtruyen.core.common.manager

fun interface StateManager<T> {
    fun onAction(action: T)
}