plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.secrets.gradle) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.ksp) apply false
    // Apply true to enable plugin at this level
    alias(libs.plugins.littlerobots.version.catalog.update) apply true
}

versionCatalogUpdate {
    sortByKey.set(true)
}