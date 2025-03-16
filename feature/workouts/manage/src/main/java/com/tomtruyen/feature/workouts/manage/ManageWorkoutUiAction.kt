package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.data.models.network.rpc.PreviousExerciseSet
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.navigation.Screen

sealed class ManageWorkoutUiAction {
    sealed class Workout : ManageWorkoutUiAction() {
        data class OnNameChanged(val name: String) : Workout()

        data class Save(val updateExistingWorkout: Boolean = false) : Workout()

        data object Delete : Workout()

        data object Discard : Workout()
    }

    sealed class Exercise : ManageWorkoutUiAction() {
        data class OnNotesChanged(val id: String, val notes: String) : Exercise()

        data object OnReorderClicked: Exercise()

        data object OnReplaceClicked : Exercise()

        data class Replace(val exercise: ExerciseUiModel) : Exercise()

        data object OnAddClicked : Exercise()

        data class Add(val exercises: List<ExerciseUiModel>) : Exercise()

        data object Delete : Exercise()

        data class Reorder(val from: Int, val to: Int) : Exercise()
    }

    sealed class Set : ManageWorkoutUiAction() {
        data class OnRepsChanged(val exerciseId: String, val setIndex: Int, val reps: String?) :
            Set()

        data class OnWeightChanged(val exerciseId: String, val setIndex: Int, val weight: String?) :
            Set()

        data class OnTimeChanged(val exerciseId: String, val setIndex: Int, val time: Int?) : Set()

        data class Delete(
            val exerciseId: String, val setIndex: Int
        ) : Set()

        data class Add(val exerciseId: String) : Set()

        data class OnToggleCompleted(
            val exerciseId: String,
            val setIndex: Int,
            val previousSet: PreviousExerciseSet?
        ) : Set()

        data class OnPreviousSetClicked(
            val exerciseId: String,
            val setIndex: Int,
            val previousSet: PreviousExerciseSet
        ): Set()
    }

    sealed class Sheet : ManageWorkoutUiAction() {
        sealed class Workout : Sheet() {
            data object Show : Workout()

            data object Dismiss : Workout()
        }

        sealed class Exercise : Sheet() {
            data class Show(val id: String? = null) : Exercise()

            data object Dismiss : Exercise()
        }

        sealed class Set : Sheet() {
            data class Show(val exerciseId: String? = null, val setIndex: Int? = null) : Set()

            data object Dismiss : Set()
        }

        sealed class Save: Sheet() {
            data object Show: Save()

            data object Dismiss: Save()
        }
    }

    sealed class Dialog: ManageWorkoutUiAction() {
        sealed class Workout: Dialog() {
            data object Show: Workout()

            data object Dismiss: Workout()
        }

        sealed class Finish: Dialog() {
            data object Show: Finish()

            data object Dismiss: Finish()
        }
    }

    sealed class Navigate : ManageWorkoutUiAction() {
        sealed class Workout : Navigate() {
            data object Execute : Workout()

            data object Edit : Workout()
        }

        sealed class Exercise : Navigate() {
            data class Detail(val id: String) : ManageWorkoutUiAction()
        }

        data object Back: Navigate()
    }

    sealed class NavResult: ManageWorkoutUiAction() {
        data class Exercises(
            val mode: ExerciseMode,
            val exercises: List<ExerciseUiModel>
        ): NavResult()

        data class ReorderWorkoutExercises(val exercises: List<WorkoutExerciseUiModel>): NavResult()
    }
}
