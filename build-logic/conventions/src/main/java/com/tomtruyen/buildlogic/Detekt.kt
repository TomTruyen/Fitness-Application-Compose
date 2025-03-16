package com.tomtruyen.buildlogic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

internal fun Project.configureDetekt(codeQualityExtension: DetektExtension) {
    val detektVersion = libs.findVersion("detekt").get().toString()

    codeQualityExtension.apply {
        toolVersion = detektVersion
        buildUponDefaultConfig = true
        config.from(files("$rootDir/config/detekt/detekt.yml"))
    }

    tasks.named<Detekt>("detekt").configure {
        description = "Runs detekt static analysis."
        jvmTarget = JavaVersion.VERSION_17.toString()

        reports {
            xml.required.set(true)
            txt.required.set(true)
            html.required.set(true)
            html.outputLocation.set(
                file("$rootDir/build/reports/detekt.html")
            )
        }

        parallel = true

        setSource(projectDir)
        include("**/*.kt", "**/*.kts")
        exclude("**/build/**", "**/generated/**", "**/resources/**")
    }
}