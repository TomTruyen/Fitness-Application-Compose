package com.tomtruyen.fitnessapplication.validation

import android.content.Context
import org.koin.core.component.KoinComponent

abstract class TextRule: KoinComponent {
    abstract val context: Context

    abstract val message: Int

    open val errorMessage: String
        get() = context.getString(message)

    abstract val validationRule: (String) -> Boolean
}
