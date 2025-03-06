package com.tomtruyen.feature.exercises.manage

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.validation.TextValidator
import com.tomtruyen.core.validation.ValidationResult
import com.tomtruyen.core.validation.rules.RequiredRule
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.feature.exercises.manage.model.ManageExerciseMode

@Immutable
data class ManageExerciseUiState(
    val initialExercise: ExerciseUiModel = ExerciseUiModel(),
    val exercise: ExerciseUiModel = ExerciseUiModel(),
    val mode: ManageExerciseMode = ManageExerciseMode.CREATE,

    val categories: List<CategoryUiModel> = emptyList(),
    val equipment: List<EquipmentUiModel> = emptyList(),

    var nameValidationResult: ValidationResult? = null,

    val loading: Boolean = false,
) {
    private val requiredValidator = TextValidator.withRules(RequiredRule())

    fun validateName(name: String) = requiredValidator.validate(name)

    fun validateAll() {
        nameValidationResult = validateName(exercise.name)
    }
}

