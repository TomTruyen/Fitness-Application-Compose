import com.android.build.api.dsl.ApplicationExtension
import com.tomtruyen.buildlogic.configureSupabase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class SupabaseApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureSupabase(extensions.getByType<ApplicationExtension>())
    }
}