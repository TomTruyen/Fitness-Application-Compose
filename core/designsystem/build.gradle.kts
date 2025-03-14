plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.core.designsystem"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.ui.text.google.fonts)
}

