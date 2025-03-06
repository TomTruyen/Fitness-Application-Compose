@file:Suppress("UnusedImport") // getKoin is considered unused import by Android Studio...

package com.tomtruyen.core.validation.rules

import android.content.Context
import com.tomtruyen.core.validation.TextRule
import org.koin.java.KoinJavaComponent.get
import com.tomtruyen.core.common.R as CommonR

class PasswordRule(
    override val context: Context = get(Context::class.java),
    override val message: Int = CommonR.string.error_weak_password
) : TextRule() {
    companion object {
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$"
    }

    override val validationRule: (String) -> Boolean = { it.matches(Regex(PASSWORD_REGEX)) }
}