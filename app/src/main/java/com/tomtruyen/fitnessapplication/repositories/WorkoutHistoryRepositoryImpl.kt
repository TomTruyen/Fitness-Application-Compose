package com.tomtruyen.fitnessapplication.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.Query
import com.tomtruyen.fitnessapplication.data.dao.WorkoutHistoryDao
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.paging.WorkoutHistoryPagingSource
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse.Companion.UPDATED_AT_ORDER_FIELD
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkoutHistoryRepositoryImpl(
    private val globalProvider: GlobalProvider,
    private val workoutHistoryDao: WorkoutHistoryDao
): WorkoutHistoryRepository() {
    private val workoutRepository by inject<WorkoutRepository>(WorkoutRepository::class.java)

    override fun findWorkoutHistoriesAsync() = workoutHistoryDao.findWorkoutHistoriesAsync()

    override fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ) = workoutHistoryDao.findWorkoutHistoriesByRange(start, end)

    override fun getWorkoutHistoriesPaginated(userId: String): Flow<PagingData<WorkoutHistoryWithWorkout>> {
        val source = WorkoutHistoryPagingSource(
            query = db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
                .document(userId)
                .collection(USER_WORKOUT_HISTORY_FIELD_NAME)
                .orderBy(UPDATED_AT_ORDER_FIELD, Query.Direction.DESCENDING),
            onSaveResponse = { histories ->
                scope.launch {
                    saveWorkoutHistoryResponses(histories)
                }
            },
        )

        return Pager(config = PagingConfig(pageSize = 1)) {
            source
        }.flow
    }

    override fun finishWorkout(
        userId: String,
        histories: List<WorkoutHistoryResponse>,
        callback: FirebaseCallback<List<WorkoutHistoryResponse>>
    ) {
        val monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM-yyyy"))

        db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
            .document(userId)
            .collection(USER_WORKOUT_HISTORY_FIELD_NAME)
            .document(monthYear)
            .set(WorkoutHistoriesResponse(histories))
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) {
                scope.launch {
                    saveWorkoutHistoryResponses(histories)
                }

                callback.onSuccess(histories)
            }
    }

    private suspend fun saveWorkoutHistoryResponses(responses: List<WorkoutHistoryResponse>) {
        val histories = responses.map { it.toWorkoutHistory() }
        val workouts = responses.map { it.workout }

        workoutRepository.saveWorkoutResponses(workouts)
        workoutHistoryDao.saveAll(histories)
    }

    companion object {
        private const val USER_WORKOUT_HISTORY_COLLECTION_NAME = "workout_history"
        private const val USER_WORKOUT_HISTORY_FIELD_NAME = "history"
    }
}