import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.tomtruyen.android.application")
    id("com.tomtruyen.android.application.compose")
    id("com.tomtruyen.firebase.application")

    alias(libs.plugins.android.git.version)
    alias(libs.plugins.google.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.secrets.gradle)
}

androidGitVersion {
    codeFormat = "MNNPPPBBB"
    format = "%tag%%.count%"
}

// Signing Configuration for Keystore
if(file(rootProject.projectDir.absolutePath + "/signing.gradle").exists()) {
    apply(
        from = rootProject.projectDir.absolutePath + "/signing.gradle"
    )
}

android {
    namespace = "com.tomtruyen.fitnessapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tomtruyen.fitnessapplication"
        minSdk = 26
        targetSdk = 34
        versionName = androidGitVersion.name()
        versionCode = androidGitVersion.code()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
//            signingConfig = signingConfigs.getByName("release")
        }

        release {
//            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:validation"))

    implementation(project(":data"))
    implementation(project(":models"))
    implementation(project(":navigation"))

    implementation(project(":feature:auth:login"))
    implementation(project(":feature:auth:register"))

    implementation(project(":feature:profile"))

    implementation(project(":feature:exercises"))
    implementation(project(":feature:exercises:create"))
    implementation(project(":feature:exercises:detail"))
    implementation(project(":feature:exercises:filter"))

    implementation(project(":feature:workouts"))
    implementation(project(":feature:workouts:create"))
    implementation(project(":feature:workouts:detail"))
    implementation(project(":feature:workouts:execute"))
    implementation(project(":feature:workouts:history"))

    // Auth
    implementation(libs.google.play.services.auth)

    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Debug Only
    debugImplementation(libs.squareup.leakcanary)
}