package com.tomtruyen.core.common.utils

import java.util.UUID

abstract class CacheKeyHelper(private val tableName: String) {
    fun createCacheKey(id: String) = "${tableName}_${id}"

    fun extractCacheId(cacheKey: String): String? {
        val exerciseId = cacheKey.removePrefix("${tableName}_")

        // Check if exerciseId is a UUID
        return try {
            UUID.fromString(exerciseId).toString()
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}