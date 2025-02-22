plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:validation"))
    implementation(project(":models"))
    implementation(project(":navigation"))

    // Images
    implementation(libs.coil.kt.compose)
}
