package com.tomtruyen.core.common.models

enum class ManageWorkoutMode {
    CREATE,
    EDIT,
    EXECUTE;

    fun isExecute() = this == EXECUTE
}