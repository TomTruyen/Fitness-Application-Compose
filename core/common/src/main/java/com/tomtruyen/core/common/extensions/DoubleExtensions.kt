package com.tomtruyen.core.common.extensions

fun Double.format() = if(this % 1 != 0.0) {
        String.format("%.0f", this)
    } else {
        String.format("%.1f", this)
}