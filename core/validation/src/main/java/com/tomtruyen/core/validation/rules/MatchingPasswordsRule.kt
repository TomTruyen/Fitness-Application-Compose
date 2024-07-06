package com.tomtruyen.core.validation.rules

import android.content.Context
import com.tomtruyen.core.common.R as CommonR
import com.tomtruyen.core.validation.TextRule
import org.koin.java.KoinJavaComponent.getKoin

class MatchingPasswordsRule(
    otherValue: String = "",
    override val context: Context = getKoin().get(),
    override val message: Int = CommonR.string.error_passwords_match
): TextRule() {
    override val validationRule: (String) -> Boolean = { it == otherValue }
}
