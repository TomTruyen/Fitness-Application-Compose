plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.core.designsystem"
}

dependencies {
    // Datastore
    implementation(libs.androidx.datastore.preferences)
}

