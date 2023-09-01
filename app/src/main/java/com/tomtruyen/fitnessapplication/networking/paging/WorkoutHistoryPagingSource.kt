package com.tomtruyen.fitnessapplication.networking.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import kotlinx.coroutines.tasks.await

class WorkoutHistoryPagingSource(
    private val query: Query,
): PagingSource<QuerySnapshot, WorkoutHistoriesResponse>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, WorkoutHistoriesResponse>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, WorkoutHistoriesResponse> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleDocument = currentPage.documents.lastOrNull()
            val nextPage = query.startAfter(lastVisibleDocument).get().await()

            LoadResult.Page(
                data = currentPage.toObjects(WorkoutHistoriesResponse::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}