package com.tomtruyen.core.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tomtruyen.core.common.R
import com.tomtruyen.core.common.providers.BuildConfigFieldProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EmailUtils: KoinComponent {
    private val buildConfigFieldProvider: BuildConfigFieldProvider by inject()

    private fun getDeviceInfo(context: Context): String {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        val deviceModel = android.os.Build.MODEL
        val deviceManufacturer = android.os.Build.MANUFACTURER
        val androidVersion = android.os.Build.VERSION.RELEASE
        val androidSDKVersion = android.os.Build.VERSION.SDK_INT

        return """
        --------------------
        Device Information:
        Model: $deviceModel
        Manufacturer: $deviceManufacturer
        Android Version: $androidVersion (API $androidSDKVersion)
        Screen Width: $screenWidth px
        Screen Height: $screenHeight px
        App Version: ${buildConfigFieldProvider.versionName} (${buildConfigFieldProvider.versionCode})
        --------------------
    """.trimIndent()
    }

    fun getEmailIntent(context: Context): Intent {
        val subject = context.getString(R.string.support_email_subject, buildConfigFieldProvider.versionName)
        val recipient = "tom.truyen@gmail.com"
        val deviceInfo = getDeviceInfo(context)

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, deviceInfo)
        }

        return emailIntent
    }
}