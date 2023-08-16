package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.validation.TextValidator
import com.tomtruyen.fitnessapplication.validation.ValidationResult
import com.tomtruyen.fitnessapplication.validation.rules.RequiredRule

data class CreateExerciseUiState(
    val initialExercise: Exercise = Exercise(),
    val exercise: Exercise = Exercise(),
    val isEditing: Boolean = false,
    var lastNameValidationResult: ValidationResult? = null,
    var lastCategoryValidationResult: ValidationResult? = null,
    var lastEquipmentValidationResult: ValidationResult? = null,
    var lastTypeValidationResult: ValidationResult? = null,
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    var nameValidationResult by mutableStateOf(lastNameValidationResult)
        private set

    var categoryValidationResult by mutableStateOf(lastCategoryValidationResult)
        private set

    var equipmentValidationResult by mutableStateOf(lastEquipmentValidationResult)
        private set

    var typeValidationResult by mutableStateOf(lastTypeValidationResult)
        private set

    fun validateName(context: Context) {
        if(exercise.name == null) return // Don't validate if email is null (not entered anything yet)

        lastNameValidationResult = requiredValidator.validate(context, exercise.name)
        nameValidationResult = lastNameValidationResult
    }

    fun validateCategory(context: Context) {
        if(exercise.category == null) return // Don't validate if email is null (not entered anything yet)

        lastCategoryValidationResult = requiredValidator.validate(context, exercise.category)
        categoryValidationResult = lastCategoryValidationResult
    }

    fun validateEquipment(context: Context) {
        if(exercise.equipment == null) return // Don't validate if email is null (not entered anything yet)

        lastEquipmentValidationResult = requiredValidator.validate(context, exercise.equipment)
        equipmentValidationResult = lastEquipmentValidationResult
    }

    fun validateType(context: Context) {
        lastTypeValidationResult = requiredValidator.validate(context, exercise.type)
        typeValidationResult = lastTypeValidationResult
    }
}

