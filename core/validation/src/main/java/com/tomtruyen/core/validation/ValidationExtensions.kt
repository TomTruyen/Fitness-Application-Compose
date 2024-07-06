package com.tomtruyen.core.validation

fun ValidationResult?.errorMessage(): String? = (this as? ValidationResult.Invalid)?.messages?.firstOrNull()

fun ValidationResult?.isValid(): Boolean = this?.passed == true
