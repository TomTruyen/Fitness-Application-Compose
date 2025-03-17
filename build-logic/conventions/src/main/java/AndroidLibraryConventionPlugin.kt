import com.android.build.gradle.LibraryExtension
import com.tomtruyen.buildlogic.configureDetekt
import com.tomtruyen.buildlogic.configureKoin
import com.tomtruyen.buildlogic.configureKotlinAndroid
import com.tomtruyen.buildlogic.getVersionAsInt
import com.tomtruyen.buildlogic.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("com.google.devtools.ksp")
            apply("org.jetbrains.kotlin.plugin.serialization")
            apply("kotlin-parcelize")
            apply("io.gitlab.arturbosch.detekt")
        }

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            configureDetekt(extensions.getByType<DetektExtension>())
            configureKoin(this)

            defaultConfig.targetSdk = libs.getVersionAsInt("sdk.target")
        }
    }
}