import com.android.build.api.dsl.ApplicationExtension
import com.tomtruyen.buildlogic.configureKoin
import com.tomtruyen.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("com.google.gms.google-services")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureKoin(this)

            defaultConfig.targetSdk = 34
        }
    }
}
