package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.repositories.BaseRepository

abstract class WorkoutHistoryRepository: BaseRepository() {
    override val identifier: String
        get() = WorkoutHistory.TABLE_NAME


}