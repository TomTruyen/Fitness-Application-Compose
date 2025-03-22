package com.tomtruyen.core.common.models.actions

import com.tomtruyen.core.common.models.BaseSet

interface SetActions {
    fun repsChanged(exerciseId: String, setIndex: Int, reps: String) = Unit
    fun weightChanged(exerciseId: String, setIndex: Int, weight: String) = Unit
    fun timeChanged(exerciseId: String, setIndex: Int, time: Int) = Unit
    fun toggleCompleted(exerciseId: String, setIndex: Int, previousSet: BaseSet?) = Unit
    fun fillPreviousSet(id: String, setIndex: Int, previousSet: BaseSet) = Unit
    fun add(exerciseId: String) = Unit
    fun delete(exerciseId: String, setIndex: Int) = Unit
    fun showSheet(exerciseId: String, setIndex: Int) = Unit
}