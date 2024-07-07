plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.feature.exercises.filter"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    implementation(project(":feature:exercises"))

    implementation(project(":data"))
    implementation(project(":navigation"))
    implementation(project(":models"))
}