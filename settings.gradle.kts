pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
include(":feature:exercises")
include(":feature:exercises:create")
include(":feature:exercises:detail")
include(":feature:exercises:filter")
include(":feature:workouts")
include(":feature:workouts:execute")
include(":feature:workouts:shared")
include(":feature:workouts:detail")
include(":feature:workouts:create")
include(":feature:workouts:history")
