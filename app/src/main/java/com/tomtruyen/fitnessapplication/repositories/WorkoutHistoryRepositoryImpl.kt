package com.tomtruyen.fitnessapplication.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.tomtruyen.fitnessapplication.data.dao.WorkoutDao
import com.tomtruyen.fitnessapplication.data.dao.WorkoutHistoryDao
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.paging.WorkoutHistoryPagingSource
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoriesResponse.Companion.UPDATED_AT_ORDER_FIELD
import com.tomtruyen.fitnessapplication.networking.models.WorkoutHistoryResponse
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class WorkoutHistoryRepositoryImpl(
    private val workoutHistoryDao: WorkoutHistoryDao,
    private val workoutDao: WorkoutDao
): WorkoutHistoryRepository() {
    private val workoutRepository by inject<WorkoutRepository>(WorkoutRepository::class.java)

    override fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ) = workoutHistoryDao.findWorkoutHistoriesByRange(start, end)

    override fun findLastEntryForWorkout(workoutId: String): Flow<WorkoutWithExercises?> {
        val lastWorkoutId = getIdWithPrefix(workoutId, FetchedData.Type.LAST_WORKOUT.identifier)

        return workoutDao.findByIdAsync(lastWorkoutId)
    }

    override fun getLastEntryForWorkout(
        userId: String,
        workoutId: String,
        callback: FirebaseCallback<Unit>
    ) = tryRequestWhenNotFetched(
        identifier = getIdWithPrefix(workoutId, FetchedData.Type.LAST_WORKOUT.identifier),
        onStopLoading = callback::onStopLoading
    ) {
        db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
            .document(userId)
            .collection(USER_WORKOUT_HISTORY_WORKOUTS_COLLECTION_NAME)
            .document(workoutId)
            .get()
            .handleCompletionResult(
                context = globalProvider.context,
                callback = callback
            ) { document ->
                val response = document.toObject(WorkoutResponse::class.java) ?: return@handleCompletionResult callback.onStopLoading()
F
                val lastWorkout = addPrefixToIds(response, FetchedData.Type.LAST_WORKOUT.identifier)
                launchWithTransaction {
                    workoutRepository.saveWorkoutResponses(listOf(lastWorkout))
                }

                callback.onSuccess(Unit)
            }
    }

    override fun getWorkoutHistoriesPaginated(userId: String): Flow<PagingData<WorkoutHistoryWithWorkout>> {
        val source = WorkoutHistoryPagingSource(
            query = db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
                .document(userId)
                .collection(USER_WORKOUT_HISTORY_FIELD_NAME)
                .orderBy(UPDATED_AT_ORDER_FIELD, Query.Direction.DESCENDING),
            onSaveResponse = { histories ->
                launchWithTransaction {
                    saveWorkoutHistoryResponses(histories)
                }
            },
        )

        // PageSize is set to 1 because we only want to load the most recent MonthYear, in a lot of cases most users won't scroll to other months
        // Especially not to months that are years ago
        return Pager(config = PagingConfig(pageSize = 1)) {
            source
        }.flow
    }

    override suspend fun finishWorkout(
        userId: String,
        history: WorkoutHistoryResponse,
        callback: FirebaseCallback<Unit>
    ) {
        val monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM-yyyy"))

        val historyDocumentRef = db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
            .document(userId)
            .collection(USER_WORKOUT_HISTORY_FIELD_NAME)
            .document(monthYear)

        updateOrSet(
            ref = historyDocumentRef,
            setData = WorkoutHistoriesResponse(listOf(history)),
            updateData = FieldValue.arrayUnion(history),
            callback = callback
        ) {
            launchWithTransaction {
                saveWorkoutHistoryResponses(listOf(history))
            }

            // Store the workout separately to be able to know what the "last workout" was for when performing next
            // We don't check this for failure because it isn't breaking the flow of the app if it fails
            db.collection(USER_WORKOUT_HISTORY_COLLECTION_NAME)
                .document(userId)
                .collection(USER_WORKOUT_HISTORY_WORKOUTS_COLLECTION_NAME)
                .document(history.id)
                .set(history)

            callback.onSuccess(Unit)
        }
    }

    private suspend fun saveWorkoutHistoryResponses(responses: List<WorkoutHistoryResponse>) = transaction {
        val responsesWithUniqueIds = responses.map { history ->
            history.apply {
                workout.id = getIdWithPrefix(workout.id, id)
            }
        }

        val histories = responsesWithUniqueIds.map {
            it.toWorkoutHistory().apply {
                workoutId = getIdWithPrefix(workoutId, FetchedData.Type.WORKOUT_HISTORY.identifier)
            }
        }

        val workouts = responsesWithUniqueIds.map { it.workout }.map {
            addPrefixToIds(it, FetchedData.Type.WORKOUT_HISTORY.identifier)
        }

        workoutRepository.saveWorkoutResponses(workouts)
        workoutHistoryDao.saveAll(histories)
    }

    /**
     * Prefixes the ids of the workout, exercises and sets with the given prefix
     * This is done so we don't need to create duplicate entities for history and last workout
     * We just reuse the workout entities and prefix the ids to make sure they don't conflict with the normal workouts
     *
     * @param lastWorkout The workout to prefix the ids of
     * @param prefix The prefix to add to the ids
     *
     * @return The workout with the prefixed ids
     */
    private fun addPrefixToIds(lastWorkout: WorkoutResponse, prefix: String): WorkoutResponse {
        return lastWorkout.apply {
            val workoutId = getIdWithPrefix(id, prefix)

            id = workoutId
            exercises = exercises.map { exercise ->
                exercise.apply {
                    val workoutExerciseId = getIdWithPrefix(id, prefix)

                    id = workoutExerciseId
                    isPerformed = true // Set performed to true this way we don't fetch it when we fetch normal workouts
                    sets = sets.map { set ->
                        set.apply {
                            id = getIdWithPrefix(id, prefix)
                            this.workoutExerciseId = workoutExerciseId
                        }
                    }
                }
            }
        }
    }

    /**
     * Prefixes the given id with the given prefix
     *
     * @param id The id to prefix
     *
     * @return The prefixed id
     */
    private fun getIdWithPrefix(id: String, prefix: String) = "${prefix}_${id}"

    companion object {
        private const val USER_WORKOUT_HISTORY_COLLECTION_NAME = "workout_history"
        private const val USER_WORKOUT_HISTORY_WORKOUTS_COLLECTION_NAME = "workouts"
        private const val USER_WORKOUT_HISTORY_FIELD_NAME = "history"
    }
}