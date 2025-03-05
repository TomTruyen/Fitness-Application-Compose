package com.tomtruyen.feature.workouts.manage.manager

fun interface StateManager<T> {
    fun onAction(action: T)
}