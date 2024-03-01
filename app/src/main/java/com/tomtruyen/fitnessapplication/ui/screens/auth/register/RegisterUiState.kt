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
    var emailValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
    var passwordValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
    var confirmPasswordValidationResult: MutableState<ValidationResult?> = mutableStateOf(null),
) {
    private val emailValidator = TextValidator.withRules(EmailRule())
    private val passwordValidator = TextValidator.withRules(PasswordRule())
    private val confirmPasswordValidator = TextValidator.withRules(PasswordRule(), MatchingPasswordsRule(password ?: ""))

    fun validateEmail(context: Context) {
        if(email == null) return // Don't validate if email is null (not entered anything yet)

        emailValidationResult.value = emailValidator.validate(context, email)
    }

    fun validatePassword(context: Context) {
        if(password == null) return // Don't validate if password is null (not entered anything yet)

        passwordValidationResult.value = passwordValidator.validate(context, password)
    }

    fun validateConfirmPassword(context: Context) {
        if(confirmPassword == null) return // Don't validate if password is null (not entered anything yet)

        confirmPasswordValidationResult.value = confirmPasswordValidator.validate(context, confirmPassword)
    }
}
