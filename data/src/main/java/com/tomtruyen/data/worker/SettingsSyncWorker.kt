package com.tomtruyen.data.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import org.koin.core.component.inject

internal class SettingsSyncWorker(
    appContext: Context,
    params: WorkerParameters
): SyncWorker<Settings>(appContext, params) {
    override val repository by inject<SettingsRepository>()
}