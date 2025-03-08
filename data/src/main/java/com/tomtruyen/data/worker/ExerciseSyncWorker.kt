package com.tomtruyen.data.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import org.koin.core.component.inject

internal class ExerciseSyncWorker(
    appContext: Context,
    params: WorkerParameters,
): SyncWorker<ExerciseWithCategoryAndEquipment>(appContext, params) {
    override val repository by inject<ExerciseRepository>()
}