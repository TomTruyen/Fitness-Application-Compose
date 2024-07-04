package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule
import org.koin.java.KoinJavaComponent.getKoin

class MatchingPasswordsRule(
    otherValue: String = "",
    override val context: Context = getKoin().get(),
    override val message: Int = R.string.error_passwords_match
): TextRule() {
    override val validationRule: (String) -> Boolean = { it == otherValue }
}
