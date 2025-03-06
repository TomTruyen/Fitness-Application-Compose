package com.tomtruyen.core.common.utils

import android.annotation.SuppressLint
import android.content.Context
import io.github.jan.supabase.storage.Storage

class ImageLoader(
    private val storage: Storage
) {
    companion object {
        private const val BUCKET_ID = "Exercises"
    }

    fun load(context: Context, url: String?) = url?.let {
        fromLocal(context, url) ?: storage.from(BUCKET_ID).publicUrl(it)
    }

    @SuppressLint("DiscouragedApi")
    private fun fromLocal(context: Context, url: String): String? {
        val resourceName = url.substringBeforeLast(".")
        val resourceId =
            context.resources.getIdentifier(resourceName, "drawable", context.packageName)

        return if (resourceId != 0) {
            "android.resource://${context.packageName}/$resourceId"
        } else null
    }
}