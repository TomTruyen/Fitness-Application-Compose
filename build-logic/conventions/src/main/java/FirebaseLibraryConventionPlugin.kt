import com.android.build.api.dsl.LibraryExtension
import com.tomtruyen.buildlogic.configureFirebase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class FirebaseLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureFirebase(extensions.getByType<LibraryExtension>())
    }
}