import com.android.build.api.dsl.ApplicationExtension
import com.tomtruyen.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("com.google.devtools.ksp")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        configureAndroidCompose(extensions.getByType<ApplicationExtension>())
    }
}