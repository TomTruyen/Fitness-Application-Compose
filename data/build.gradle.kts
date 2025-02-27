plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.supabase.library")
}

android {
    namespace = "com.tomtruyen.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":models"))

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
