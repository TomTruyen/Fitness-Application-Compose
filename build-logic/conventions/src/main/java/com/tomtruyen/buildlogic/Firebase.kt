package com.tomtruyen.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        dependencies {
            val bom = libs.findLibrary("google-firebase-bom").get()
            add("implementation", platform(bom))

            add("implementation", libs.findLibrary("google.firebase.analytics").get())
            add("implementation", libs.findLibrary("google.firebase.auth").get())
            add("implementation", libs.findLibrary("google.firebase.crashlytics").get())
            add("implementation", libs.findLibrary("google.firebase.encoders").get())
            add("implementation", libs.findLibrary("google.firebase.firestore").get())

            add("implementation", libs.findLibrary("androidx.credentials").get())
            add("implementation", libs.findLibrary("androidx.credentials.play.services.auth").get())
            add("implementation", libs.findLibrary("google.identity.googleid").get())
        }
    }
}