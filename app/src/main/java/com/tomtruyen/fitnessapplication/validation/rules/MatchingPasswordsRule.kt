package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule

class MatchingPasswordsRule(
    otherValue: String = "",
    override val errorMessage: (context: Context) -> String = { it.getString(R.string.error_passwords_match) },
): TextRule {
    override val validationRule: (String) -> Boolean = { it == otherValue }
}
