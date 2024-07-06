dependencyResolutionManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()

        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"

include(":conventions")