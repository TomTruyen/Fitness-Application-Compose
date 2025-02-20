plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.feature.workouts.shared"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    implementation(project(":data"))
    implementation(project(":models"))

    // Images
    implementation(libs.coil.kt.compose)
}