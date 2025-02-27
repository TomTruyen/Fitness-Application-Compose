plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
    id("com.tomtruyen.supabase.library")
}

android {
    namespace = "com.tomtruyen.feature.auth"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))

    implementation(project(":data"))
    implementation(project(":models"))
}