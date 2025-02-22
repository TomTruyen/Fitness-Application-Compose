package com.tomtruyen.feature.exercises.create.model

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