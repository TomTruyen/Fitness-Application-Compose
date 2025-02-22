package com.tomtruyen.feature.exercises.create

import com.tomtruyen.core.validation.TextValidator
import com.tomtruyen.core.validation.ValidationResult
import com.tomtruyen.core.validation.rules.RequiredRule
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.feature.exercises.create.model.ManageExerciseMode

data class CreateExerciseUiState(
    val initialExercise: Exercise = Exercise(),
    val exercise: Exercise = Exercise(),
    val mode: ManageExerciseMode = ManageExerciseMode.CREATE,

    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),

    var nameValidationResult: ValidationResult? = null,

    val loading: Boolean = false,
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    fun validateName(name: String) = requiredValidator.validate(name)

    fun validateAll() {
        nameValidationResult = validateName(exercise.name.orEmpty())
    }
}

