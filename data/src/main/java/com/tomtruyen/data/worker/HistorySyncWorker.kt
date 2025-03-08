package com.tomtruyen.data.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import org.koin.core.component.inject

internal class HistorySyncWorker(
    appContext: Context,
    params: WorkerParameters
): SyncWorker<WorkoutHistoryWithExercises>(appContext, params) {
    override val repository by inject<HistoryRepository>()
}