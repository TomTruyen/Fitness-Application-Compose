package com.tomtruyen.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureSupabase(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        dependencies {
            // Required by Supabase
            add("implementation", libs.findLibrary("ktor").get())

            // Supabase
            val bom = libs.findLibrary("supabase-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("supabase-postgrest").get())
            add("implementation", libs.findLibrary("supabase-auth").get())
            add("implementation", libs.findLibrary("supabase-storage").get())

            // Google Auth
            add("implementation", libs.findLibrary("androidx.credentials").get())
            add("implementation", libs.findLibrary("androidx.credentials.play.services.auth").get())
            add("implementation", libs.findLibrary("google.identity.googleid").get())
        }
    }
}