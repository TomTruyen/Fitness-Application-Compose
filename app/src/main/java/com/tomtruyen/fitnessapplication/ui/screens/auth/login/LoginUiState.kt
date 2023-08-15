package com.tomtruyen.fitnessapplication.ui.screens.auth.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tomtruyen.fitnessapplication.validation.TextValidator
import com.tomtruyen.fitnessapplication.validation.ValidationResult
import com.tomtruyen.fitnessapplication.validation.rules.EmailRule
import com.tomtruyen.fitnessapplication.validation.rules.RequiredRule

data class LoginUiState(
    val email: String? = null,
    val password: String? = null,
    var lastEmailValidationResult: ValidationResult? = null,
    var lastPasswordValidationResult: ValidationResult? = null
) {
    private val emailValidator = TextValidator.withRules(EmailRule())
    private val passwordValidator = TextValidator.withRules(RequiredRule())

    var emailValidationResult by mutableStateOf(lastEmailValidationResult)
        private set

    var passwordValidationResult by mutableStateOf(lastPasswordValidationResult)
        private set

    fun validateEmail(context: Context) {
        if(email == null) return // Don't validate if email is null (not entered anything yet)

        lastEmailValidationResult = emailValidator.validate(context, email)
        emailValidationResult = lastEmailValidationResult
    }

    fun validatePassword(context: Context) {
        if(password == null) return // Don't validate if password is null (not entered anything yet)

        lastPasswordValidationResult = passwordValidator.validate(context, password)
        passwordValidationResult = lastPasswordValidationResult
    }
}
