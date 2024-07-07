package com.tomtruyen.core.common.extensions

fun String.capitalize() = this.lowercase().replaceFirstChar { it.uppercase() }