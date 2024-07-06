package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.core.validation.TextValidator
import com.tomtruyen.core.validation.ValidationResult
import com.tomtruyen.core.validation.rules.RequiredRule

data class CreateExerciseUiState(
    val initialExercise: com.tomtruyen.data.entities.Exercise = com.tomtruyen.data.entities.Exercise(),
    val exercise: com.tomtruyen.data.entities.Exercise = com.tomtruyen.data.entities.Exercise(),
    val isEditing: Boolean = false,

    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),

    var nameValidationResult: ValidationResult? = null,
    var categoryValidationResult: ValidationResult? = null,
    var typeValidationResult: ValidationResult? = null,

    val loading: Boolean = false,
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    fun validateName(name: String) = requiredValidator.validate(name)
    fun validateCategory(category: String) = requiredValidator.validate(category)
    fun validateType(type: String) = requiredValidator.validate(type)
}

