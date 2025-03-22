package com.tomtruyen.core.common.models

interface ExerciseSet: BaseSet {
    val id: String
    override val reps: Int?
    override val weight: Double?
    override val time: Int?
    override val sortOrder: Int
    val completed: Boolean
    val changeRecord: Set<ChangeType>
}