package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule

class EmailRule(
    override val errorMessage: (context: Context) -> String = { it.getString(R.string.error_invalid_email) },
) : TextRule {
    companion object {
        private const val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$"
    }

    override val validationRule: (String) -> Boolean = { it.matches(Regex(EMAIL_REGEX)) }
}
