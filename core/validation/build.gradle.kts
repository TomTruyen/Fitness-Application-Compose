plugins {
    id("com.tomtruyen.android.library")
}

android {
    namespace = "com.tomtruyen.core.validation"
}

dependencies {
    implementation(project(":core:common"))
}