@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.googleServices)
}

sonar {
    properties {
        property("sonar.projectKey", "tom-truyen_fitness-application")
        property("sonar.organization", "tom-truyen")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

// Signing Configuration for Keystore
if(file(rootProject.projectDir.absolutePath + "/signing.gradle").exists()) {
    apply(
        from = rootProject.projectDir.absolutePath + "/signing.gradle"
    )
}

android {
    namespace = "com.tomtruyen.fitnessapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tomtruyen.fitnessapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["release"]
        }

        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // Koin
    implementation(libs.koin)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(libs.play.services.auth)

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