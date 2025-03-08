import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
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
        registerSupabaseConventionPlugin()
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

fun NamedDomainObjectContainer<PluginDeclaration>.registerSupabaseConventionPlugin() {
    register("supabaseLibrary") {
        id = "com.tomtruyen.supabase.library"
        implementationClass = "SupabaseLibraryConventionPlugin"
    }

    register("supabaseLibraryCompose") {
        id = "com.tomtruyen.supabase.application"
        implementationClass = "SupabaseApplicationConventionPlugin"
    }
}