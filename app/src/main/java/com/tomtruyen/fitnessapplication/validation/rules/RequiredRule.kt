package com.tomtruyen.fitnessapplication.validation.rules

import android.content.Context
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.validation.TextRule

class RequiredRule(
    override val errorMessage: (context: Context) -> String = { it.getString(R.string.error_required) }
): TextRule {
    override val validationRule: (String) -> Boolean = { it.isNotEmpty() && it.isNotBlank() }
}
