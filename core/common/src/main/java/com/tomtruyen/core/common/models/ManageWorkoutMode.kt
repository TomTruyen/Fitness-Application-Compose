package com.tomtruyen.core.common.models

enum class ManageWorkoutMode {
    CREATE,
    EDIT,
    EXECUTE,
    VIEW;

    val isExecute: Boolean
        get() = this == EXECUTE

    val isCreate: Boolean
        get() = this == CREATE

    val isEdit: Boolean
        get() = this == EDIT

    val isView: Boolean
        get() = this == VIEW
}