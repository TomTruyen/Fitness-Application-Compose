package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.WorkoutHistoryDao
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryRepositoryImpl(
    private val workoutHistoryDao: WorkoutHistoryDao,
) : WorkoutHistoryRepository() {
    // TODO: Rework this with Supabase and use proper caching using the `fetch` and `launchCacheTransaction`
}