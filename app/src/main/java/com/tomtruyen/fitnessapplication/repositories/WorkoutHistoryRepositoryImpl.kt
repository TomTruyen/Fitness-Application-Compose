package com.tomtruyen.fitnessapplication.repositories

import com.google.firebase.firestore.SetOptions
import com.tomtruyen.fitnessapplication.data.dao.WorkoutHistoryDao
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
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
    private var historyDocuments: List<String>? = null

    private val workoutRepository by inject<WorkoutRepository>(WorkoutRepository::class.java)

    override fun findWorkoutHistoriesAsync() = workoutHistoryDao.findWorkoutHistoriesAsync()

    override fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ) = workoutHistoryDao.findWorkoutHistoriesByRange(start, end)

    override fun getWorkoutHistoryDocuments(userId: String, callback: FirebaseCallback<Unit>) = tryRequestWhenNotFetched(
        identifier = FetchedData.Type.WORKOUT_HISTORY_DOCUMENTS.identifier,
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
            .document(userId)
            .collection(USER_WORKOUT_HISTORY_FIELD_NAME)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback,
                setFetchSuccessful = {
                    setFetchSuccessful(FetchedData.Type.WORKOUT_HISTORY_DOCUMENTS.identifier)
                }
            ) {
                historyDocuments = it.documents.map { doc -> doc.id }

                callback.onSuccess(Unit)
            }
    }

    override fun getWorkoutHistoriesPerMonth(
        userId: String,
        callback: FirebaseCallback<WorkoutHistoriesResponse>
    ) {
        // Us ethe documents that were fetched by the getWorkoutHistoryDocuments function
        // To fetch the actual data

        // On startup: always fetch the latest known document
        // On the History page actually fetch the other documents paginated
        // When on end of list, fetch the next document
        // Do use the tryRequestWhenNotFetched function
        // The identifier is the monthyear
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