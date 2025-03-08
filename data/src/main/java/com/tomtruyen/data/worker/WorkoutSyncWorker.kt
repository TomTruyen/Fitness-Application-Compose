package com.tomtruyen.data.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import org.koin.core.component.inject

internal class WorkoutSyncWorker(
    appContext: Context,
    params: WorkerParameters
): SyncWorker<WorkoutWithExercises>(appContext, params)  {
    override val repository by inject<WorkoutRepository>()
}