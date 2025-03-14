plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
}

android {
    namespace = "com.tomtruyen.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)

    implementation(project(":core:common"))
    implementation(project(":data"))
}
