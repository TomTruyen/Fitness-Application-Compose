@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.secretsGradle)
    alias(libs.plugins.ksp)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.kapt)
    alias(libs.plugins.androidGitVersion)
    id("kotlin-parcelize")
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
            signingConfig = signingConfigs.getByName("release")
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose + Material3
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)

    // Gson
    implementation(libs.gson)

    // Koin
    implementation(libs.koin)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(libs.play.services.auth)

    // Room
    implementation(libs.room)
    kapt(libs.room.compiler)

    // Raamcosta Compose Navigation
    implementation(libs.raamcosta.compose.destinations)
    ksp(libs.raamcosta.compose.destinations.ksp)

    // Glide
    implementation(libs.glide)

    // Reorderable (Drag & Drop) Lazy Column
    implementation(libs.reorderable)

    // Paging 3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.koin.test.junit)

    // Android Testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    // Debug Only
    debugImplementation(libs.leakcanary)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}