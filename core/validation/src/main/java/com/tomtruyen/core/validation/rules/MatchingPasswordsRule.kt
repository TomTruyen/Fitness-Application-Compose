@file:Suppress("UnusedImport") // getKoin is considered unused import by Android Studio...

package com.tomtruyen.core.validation.rules

import android.content.Context
import com.tomtruyen.core.common.utils.EmailUtils.getKoin
import com.tomtruyen.core.validation.TextRule
import com.tomtruyen.core.common.R as CommonR

class MatchingPasswordsRule(
    otherValue: String = "",
    override val context: Context = getKoin().get(),
    override val message: Int = CommonR.string.error_passwords_match
) : TextRule() {
    override val validationRule: (String) -> Boolean = { it == otherValue }
}
