package com.tomtruyen.feature.exercises.manage

import com.tomtruyen.core.validation.TextValidator
import com.tomtruyen.core.validation.ValidationResult
import com.tomtruyen.core.validation.rules.RequiredRule
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.feature.exercises.manage.model.ManageExerciseMode

data class ManageExerciseUiState(
    val initialExercise: Exercise = Exercise(),
    val exercise: Exercise = Exercise(),
    val mode: ManageExerciseMode = ManageExerciseMode.CREATE,

    val categories: List<Category> = emptyList(),
    val equipment: List<Equipment> = emptyList(),

    var nameValidationResult: ValidationResult? = null,

    val loading: Boolean = false,
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    fun validateName(name: String) = requiredValidator.validate(name)

    fun validateAll() {
        nameValidationResult = validateName(exercise.name.orEmpty())
    }
}

