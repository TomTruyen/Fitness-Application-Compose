package com.tomtruyen.core.validation.rules

import android.content.Context
import com.tomtruyen.core.validation.TextRule
import org.koin.java.KoinJavaComponent.get
import com.tomtruyen.core.common.R as CommonR

class EmailRule(
    override val context: Context = get(Context::class.java),
    override val message: Int = CommonR.string.error_invalid_email
) : TextRule() {
    companion object {
        private const val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$"
    }

    override val validationRule: (String) -> Boolean = { it.matches(Regex(EMAIL_REGEX)) }
}
