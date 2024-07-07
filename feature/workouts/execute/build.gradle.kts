plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.android.library.compose")
    id("com.tomtruyen.firebase.library")
}

android {
    namespace = "com.tomtruyen.feature.workouts.execute"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    implementation(project(":data"))
    implementation(project(":navigation"))
    implementation(project(":models"))

    implementation(project(":feature:workouts:shared"))
}