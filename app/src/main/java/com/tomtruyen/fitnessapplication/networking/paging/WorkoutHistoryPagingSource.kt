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
            val currentDocument = params.key ?: query.get().await()
            val lastVisibleDocument = currentDocument.documents.lastOrNull()

            val nextDocument = lastVisibleDocument?.let {
                query.startAfter(it).get().await()
            }

            val data = currentDocument.toObjects(WorkoutHistoriesResponse::class.java).flatMap { it.data }

            // Is the documents id/name e.g.: "February-2023"
            val documentId = lastVisibleDocument?.reference?.id

            // TODO: Add Caching
            // All documents should only be fetched once. After that we should just return them from the database
            // The best way is to probably have another table "WorkoutHistoryRemoteKeys"
            // That database should have all ids of the histories
            // It should also have as key the name of the document
            // It should also state whether there is a next document or not

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
            Log.d("@@@", "Error: $e")
            LoadResult.Error(e)
        }
    }
}