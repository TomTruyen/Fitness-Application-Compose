package com.tomtruyen.core.common.extensions

import java.util.Locale

fun Double.format() = if (this % 1 != 0.0) {
    String.format(Locale.getDefault(), "%.0f", this)
} else {
    String.format(Locale.getDefault(), "%.1f", this)
}