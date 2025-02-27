package com.tomtruyen.data.firebase.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tomtruyen.data.firebase.models.WorkoutHistoriesResponse
import com.tomtruyen.data.firebase.models.WorkoutHistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

//class WorkoutHistoryPagingSource(
//    private val query: Query,
//    private val onSaveResponse: (List<WorkoutHistoryResponse>) -> Job
//): PagingSource<QuerySnapshot, com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>() {
//    private val workoutHistoryDao by inject<com.tomtruyen.data.dao.WorkoutHistoryDao>(com.tomtruyen.data.dao.WorkoutHistoryDao::class.java)
//
//    override fun getRefreshKey(state: PagingState<QuerySnapshot, com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>): QuerySnapshot? = null
//
//    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, com.tomtruyen.data.entities.WorkoutHistoryWithWorkout> {
//        return try {
//            val currentDocument = params.key ?: query.get().await() // get().await() is only executed on the first page
//
//            // Get the first document -> Most recent Month Year
//            val firstVisibleDocument = currentDocument.documents.firstOrNull()
//
//            val nextDocument = firstVisibleDocument?.let {
//                query.startAfter(it).get().await()
//            }
//
//            val data = firstVisibleDocument?.toObject(WorkoutHistoriesResponse::class.java)?.data.orEmpty()
//
//            val savedHistories = withContext(Dispatchers.IO) {
//                onSaveResponse(data).join()
//                workoutHistoryDao.findWorkoutHistoriesByIds(data.map { it.id })
//            }
//
//            LoadResult.Page(
//                data = savedHistories,
//                prevKey = null,
//                nextKey = nextDocument
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//}