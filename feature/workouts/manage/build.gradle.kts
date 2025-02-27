plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
    id("com.tomtruyen.supabase.library")
}

android {
    namespace = "com.tomtruyen.feature.workouts.manage"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:validation"))

    implementation(project(":data"))
    implementation(project(":navigation"))
    implementation(project(":models"))

    // Reorderable (Drag & Drop) Lazy Column
    implementation(libs.reorderable)
}