package com.tomtruyen.fitnessapplication.networking.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.tomtruyen.fitnessapplication.data.dao.WorkoutHistoryDao
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class WorkoutHistoryPagingSource(
    private val query: Query,
    private val onSaveResponse: (List<WorkoutHistoryResponse>) -> Unit
): PagingSource<QuerySnapshot, WorkoutHistoryWithWorkout>() {
    private val workoutHistoryDao by inject<WorkoutHistoryDao>(WorkoutHistoryDao::class.java)

    override fun getRefreshKey(state: PagingState<QuerySnapshot, WorkoutHistoryWithWorkout>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, WorkoutHistoryWithWorkout> {
        return try {
            val currentDocument = params.key ?: query.get().await() // get().await() is only executed on the first page

            // Get the first document -> Most recent Month Year
            val firstVisibleDocument = currentDocument.documents.firstOrNull()

            val nextDocument = firstVisibleDocument?.let {
                query.startAfter(it).get().await()
            }

            val data = currentDocument.toObjects(WorkoutHistoriesResponse::class.java).flatMap { it.data }

            val savedHistories = withContext(Dispatchers.IO) {
                onSaveResponse(data)
                workoutHistoryDao.findWorkoutHistoriesByIds(data.map { it.id })
            }

            LoadResult.Page(
                data = savedHistories,
                prevKey = null,
                nextKey = nextDocument
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}