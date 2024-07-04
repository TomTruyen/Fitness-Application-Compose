package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule
import org.koin.java.KoinJavaComponent.getKoin

class RequiredRule(
    override val context: Context = getKoin().get(),
    override val message: Int = R.string.error_required
): TextRule() {
    override val validationRule: (String) -> Boolean = { it.isNotEmpty() && it.isNotBlank() }
}
