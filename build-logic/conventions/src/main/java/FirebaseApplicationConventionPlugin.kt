import com.android.build.api.dsl.ApplicationExtension
import com.tomtruyen.buildlogic.configureFirebase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class FirebaseApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureFirebase(extensions.getByType<ApplicationExtension>())
    }
}