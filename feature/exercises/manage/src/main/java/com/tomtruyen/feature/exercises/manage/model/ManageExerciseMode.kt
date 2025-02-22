package com.tomtruyen.feature.exercises.manage.model

enum class ManageExerciseMode {
    CREATE,
    EDIT;

    companion object {
        fun fromArgs(id: String?) = when(id != null) {
            true -> EDIT
            false -> CREATE
        }
    }
}