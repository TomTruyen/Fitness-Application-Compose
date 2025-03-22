package com.tomtruyen.core.common.models.actions

interface ExerciseActions {
    fun navigateDetail(id: String) = Unit
    fun showSheet(id: String) = Unit
    fun notesChanged(id: String, notes: String) = Unit
}