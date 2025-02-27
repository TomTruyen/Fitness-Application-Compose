import com.android.build.api.dsl.LibraryExtension
import com.tomtruyen.buildlogic.configureSupabase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class SupabaseLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        configureSupabase(extensions.getByType<LibraryExtension>())
    }
}