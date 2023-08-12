package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule
class PasswordRule(
    override val errorMessage: (context: Context) -> String = { it.getString(R.string.error_weak_password) },
) : TextRule {
    companion object {
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
    }

    override val validationRule: (String) -> Boolean = { it.matches(Regex(PASSWORD_REGEX)) }
}