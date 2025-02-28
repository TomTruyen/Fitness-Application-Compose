package com.tomtruyen.core.common.utils

import io.github.jan.supabase.storage.Storage

class ImageLoader(
    private val storage: Storage
) {
    companion object {
        private const val BUCKET_ID = "Exercises"
    }

    fun load(url: String?) = url?.let { storage.from(BUCKET_ID).publicUrl(it) }
}