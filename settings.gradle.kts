pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "Fitness Application"
include(":app")
include(":core")
include(":core:validation")
include(":core:common")
include(":core:designsystem")
include(":data")
include(":networking")
include(":models")
include(":navigation")
include(":core:ui")
include(":feature")
include(":feature:auth")
include(":feature:auth:login")
include(":feature:auth:register")
include(":feature:profile")
