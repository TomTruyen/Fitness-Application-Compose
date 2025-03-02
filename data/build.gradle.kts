plugins {
    id("com.tomtruyen.android.library")
    id("com.tomtruyen.supabase.library")
}

android {
    namespace = "com.tomtruyen.data"
}

dependencies {
    implementation(project(":core:common"))

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
