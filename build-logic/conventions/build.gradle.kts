import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.tomtruyen.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.google.ksp.plugin)
}

gradlePlugin {
    plugins {
        registerApplicationConventionPlugins()
        registerLibraryConventionPlugins()
    }
}

fun NamedDomainObjectContainer<PluginDeclaration>.registerApplicationConventionPlugins() {
    register("androidApplication") {
        id = "com.tomtruyen.android.application"
        implementationClass = "AndroidApplicationConventionPlugin"
    }

    register("androidApplicationCompose") {
        id = "com.tomtruyen.android.application.compose"
        implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
}

fun NamedDomainObjectContainer<PluginDeclaration>.registerLibraryConventionPlugins() {
    register("androidLibrary") {
        id = "com.tomtruyen.android.library"
        implementationClass = "AndroidLibraryConventionPlugin"
    }

    register("androidLibraryCompose") {
        id = "com.tomtruyen.android.library.compose"
        implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
}