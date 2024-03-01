package com.tomtruyen.fitnessapplication.validation

import androidx.compose.runtime.MutableState

fun MutableState<ValidationResult?>.errorMessage(): String? = value.errorMessage()

fun MutableState<ValidationResult?>.isValid(): Boolean = value.isValid()

fun ValidationResult?.errorMessage(): String? = (this as? ValidationResult.Invalid)?.messages?.firstOrNull()

fun ValidationResult?.isValid(): Boolean = this?.passed == true
