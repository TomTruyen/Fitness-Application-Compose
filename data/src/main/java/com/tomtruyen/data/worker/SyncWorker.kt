package com.tomtruyen.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tomtruyen.data.repositories.interfaces.SyncRepository
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.get

internal abstract class SyncWorker<T>(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(
    appContext = appContext,
    params = params
), KoinComponent {
    abstract val repository: SyncRepository<T>

    override suspend fun doWork(): Result {
        Log.i(TAG, "Starting Sync Task")

        val items = repository.findSyncItems()

        items.forEach { item ->
            try {
                Log.i(TAG, "Syncing Item: $item")
                repository.sync(item)
            } catch (e: Exception) {
                Log.e(TAG, "Syncing of $item failed. Retrying...", e)
                return Result.retry()
            }
        }

        Log.i(TAG, "Syncing Finished")
        return Result.success()
    }

    companion object {
        private const val TAG = "SyncWorker"

        inline fun <reified T : SyncWorker<*>> schedule(context: Context = get(Context::class.java)) {
            val request = OneTimeWorkRequestBuilder<T>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                ).build()

            Log.i(TAG, "Sync Scheduled with ${T::class.simpleName}")
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}