package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.core.common.models.BaseSet
import com.tomtruyen.core.common.models.actions.SetActions

class WorkoutSetActions(
    private val onAction: (ManageWorkoutUiAction) -> Unit
): SetActions {
    override fun repsChanged(exerciseId: String, setIndex: Int, reps: String) = onAction(
        ManageWorkoutUiAction.Set.OnRepsChanged(
            exerciseId = exerciseId,
            setIndex = setIndex,
            reps = reps
        )
    )

    override fun weightChanged(exerciseId: String, setIndex: Int, weight: String) = onAction(
        ManageWorkoutUiAction.Set.OnWeightChanged(
            exerciseId = exerciseId,
            setIndex = setIndex,
            weight = weight
        )
    )

    override fun timeChanged(exerciseId: String, setIndex: Int, time: Int) = onAction(
        ManageWorkoutUiAction.Set.OnTimeChanged(
            exerciseId = exerciseId,
            setIndex = setIndex,
            time = time
        )
    )

    override fun toggleCompleted(exerciseId: String, setIndex: Int, previousSet: BaseSet?) = onAction(
        ManageWorkoutUiAction.Set.OnToggleCompleted(
            exerciseId = exerciseId,
            setIndex = setIndex,
            previousSet = previousSet
        )
    )

    override fun fillPreviousSet(id: String, setIndex: Int, previousSet: BaseSet) = onAction(
        ManageWorkoutUiAction.Set.OnPreviousSetClicked(
            exerciseId = id,
            setIndex = setIndex,
            previousSet = previousSet
        )
    )

    override fun add(exerciseId: String) = onAction(
        ManageWorkoutUiAction.Set.Add(
            exerciseId = exerciseId
        )
    )

    override fun delete(exerciseId: String, setIndex: Int) = onAction(
        ManageWorkoutUiAction.Set.Delete(
            exerciseId = exerciseId,
            setIndex = setIndex
        )
    )

    override fun showSheet(exerciseId: String, setIndex: Int) = onAction(
        ManageWorkoutUiAction.Sheet.Set.Show(
            exerciseId = exerciseId,
            setIndex = setIndex
        )
    )
}