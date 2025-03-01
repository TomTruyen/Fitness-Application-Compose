package com.tomtruyen.data.extensions

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException

/**
 * Supabase does not support transactions yet without using RPC. However RPC's are hard to maintain so not useful for us
 *
 * This is a basic unofficial workaround where simply perform our calls and then we delete the "Main" table item which will cascade to others
 */
suspend fun SupabaseClient.transaction(onRollback: suspend () -> Unit, block: suspend () -> Unit) = try {
    block()
} catch (e: RestException) {
    e.printStackTrace()

    onRollback()
}