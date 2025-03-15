package com.tomtruyen.core.common

import kotlinx.serialization.json.Json

val JsonInstance by lazy {
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}