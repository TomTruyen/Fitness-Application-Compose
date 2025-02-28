package com.tomtruyen.buildlogic

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.getSecret(key: String): String? {
    val secretsFile = rootProject.file("secrets.properties")
    val secretsProperties = Properties()

    if (!secretsFile.exists()) {
        throw GradleException("secrets.properties file is missing. Please create one in the root of the project.")
    }

    secretsProperties.load(secretsFile.inputStream())

    return secretsProperties.getProperty(key)
}