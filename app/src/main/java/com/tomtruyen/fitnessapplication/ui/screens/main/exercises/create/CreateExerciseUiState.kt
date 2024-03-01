package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import android.content.Context
import androidx.compose.runtime.MutableState
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
    var nameValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
    var categoryValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
    var typeValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    fun validateName(context: Context) {
        if(exercise.name == null) return // Don't validate if email is null (not entered anything yet)

        nameValidationResult.value = requiredValidator.validate(context, exercise.name)
    }

    fun validateCategory(context: Context) {
        if(exercise.category == null) return // Don't validate if email is null (not entered anything yet)

        categoryValidationResult.value = requiredValidator.validate(context, exercise.category)
    }

    fun validateType(context: Context) {
        typeValidationResult.value = requiredValidator.validate(context, exercise.type)
    }
}

