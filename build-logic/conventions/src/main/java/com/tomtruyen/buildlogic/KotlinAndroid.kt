package com.tomtruyen.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = libs.getVersionAsInt("sdk.compile")

        defaultConfig {
            minSdk = libs.getVersionAsInt("sdk.min")
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(libs.getVersionAsInt("java"))
            targetCompatibility = JavaVersion.toVersion(libs.getVersionAsInt("java"))
            isCoreLibraryDesugaringEnabled = true
        }

        packaging {
            resources {
                excludes += "/META-INF/LICENSE.md"
                excludes += "/META-INF/LICENSE-notice.md"
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.getVersionAsInt("java").toString()))
            allWarningsAsErrors.set(false)

            freeCompilerArgs.add("-Xwhen-guards")
        }
    }

    dependencies {
        add("implementation", libs.findLibrary("androidx.core.ktx").get())

        // Serialization
        add("implementation", libs.findLibrary("kotlinx.serialization.json").get())

        add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())

        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}
