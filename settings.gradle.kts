import org.gradle.kotlin.dsl.flatDir
import org.gradle.kotlin.dsl.repositories

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        flatDir{
            dirs("app/libs")   // look into app/libs
        }
    }
}

rootProject.name = "pamflet"
include(":app")
 