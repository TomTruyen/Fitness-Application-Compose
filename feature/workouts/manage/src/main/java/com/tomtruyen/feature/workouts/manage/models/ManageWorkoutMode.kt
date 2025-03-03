package com.tomtruyen.feature.workouts.manage.models

enum class ManageWorkoutMode {
    CREATE,
    EDIT,
    EXECUTE;

    fun isExecute() = this == EXECUTE

    companion object {
        fun fromArgs(id: String?, execute: Boolean) = when {
            id == null -> CREATE
            execute -> EXECUTE
            else -> EDIT
        }
    }
}