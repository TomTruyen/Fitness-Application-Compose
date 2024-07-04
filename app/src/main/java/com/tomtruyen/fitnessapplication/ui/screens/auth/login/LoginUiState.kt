package com.tomtruyen.fitnessapplication.ui.screens.auth.login

import com.tomtruyen.fitnessapplication.validation.TextValidator
import com.tomtruyen.fitnessapplication.validation.ValidationResult
import com.tomtruyen.fitnessapplication.validation.rules.EmailRule
import com.tomtruyen.fitnessapplication.validation.rules.RequiredRule

data class LoginUiState(
    val email: String? = null,
    val password: String? = null,
    var emailValidationResult: ValidationResult? = null,
    var passwordValidationResult: ValidationResult? = null,

    val loading: Boolean = false
) {
    private val emailValidator = TextValidator.withRules(EmailRule())
    private val passwordValidator = TextValidator.withRules(RequiredRule())

    fun validateEmail(email: String) = emailValidator.validate(email)
    fun validatePassword(password: String) = passwordValidator.validate(password)
}
