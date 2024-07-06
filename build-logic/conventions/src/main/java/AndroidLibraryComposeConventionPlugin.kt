import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.tomtruyen.buildlogic.configureAndroidCompose
import com.tomtruyen.buildlogic.configureKoin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        configureAndroidCompose(extensions.getByType<LibraryExtension>())
        configureKoin(extensions.getByType<LibraryExtension>())
    }
}