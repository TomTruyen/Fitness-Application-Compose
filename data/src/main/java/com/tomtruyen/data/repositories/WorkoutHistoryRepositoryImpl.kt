package com.tomtruyen.data.repositories

import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryRepositoryImpl: WorkoutHistoryRepository() {
    private val dao = database.workoutHistoryDao()

    // TODO: Rework this with Supabase and use proper caching using the `fetch` and `launchCacheTransaction`
}