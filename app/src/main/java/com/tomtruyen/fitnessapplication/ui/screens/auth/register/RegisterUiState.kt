package com.tomtruyen.fitnessapplication.ui.screens.auth.register

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tomtruyen.fitnessapplication.validation.TextValidator
import com.tomtruyen.fitnessapplication.validation.ValidationResult
import com.tomtruyen.fitnessapplication.validation.rules.EmailRule
import com.tomtruyen.fitnessapplication.validation.rules.MatchingPasswordsRule
import com.tomtruyen.fitnessapplication.validation.rules.PasswordRule

data class RegisterUiState(
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,

    var emailValidationResult: ValidationResult? = null,
    var passwordValidationResult: ValidationResult? = null,
    var confirmPasswordValidationResult: ValidationResult? = null,

    val loading: Boolean = false,
) {
    private val emailValidator = TextValidator.withRules(EmailRule())
    private val passwordValidator = TextValidator.withRules(PasswordRule())
    private val confirmPasswordValidator = TextValidator.withRules(PasswordRule(), MatchingPasswordsRule(password.orEmpty()))

    fun validateEmail(email: String) = emailValidator.validate(email)
    fun validatePassword(password: String) = passwordValidator.validate(password)
    fun validateConfirmPassword(confirmPassword: String) = confirmPasswordValidator.validate(confirmPassword)
}
