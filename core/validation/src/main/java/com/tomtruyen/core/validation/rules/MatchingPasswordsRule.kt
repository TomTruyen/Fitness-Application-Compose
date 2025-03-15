package com.tomtruyen.core.validation.rules

import android.content.Context
import com.tomtruyen.core.validation.TextRule
import org.koin.java.KoinJavaComponent.get
import com.tomtruyen.core.common.R as CommonR

class MatchingPasswordsRule(
    otherValue: String = "",
    override val context: Context = get(Context::class.java),
    override val message: Int = CommonR.string.error_passwords_match
) : TextRule() {
    override val validationRule: (String) -> Boolean = { it == otherValue }
}
