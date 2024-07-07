package com.tomtruyen.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKoin(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("insert.koin.compose").get())
            add("implementation", libs.findLibrary("insert.koin.compose.navigation").get())
            add("implementation", libs.findLibrary("insert.koin.core").get())
        }
    }
}