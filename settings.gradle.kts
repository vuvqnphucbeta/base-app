import java.util.Properties
import kotlin.apply

val localProperties = Properties().apply {
    val localPropertiesFile = file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

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
        maven("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven("https://artifact.bytedance.com/repository/pangle/")
        maven {
            url = uri("https://maven.pkg.github.com/thinhnvbetasoft/Android-Library-Private")
            credentials {
                username = "thinhnvbetasoft"
                password = localProperties.getProperty("keyPackageGit", "")
            }
        }
    }
}

rootProject.name = "Base-Project"
include(":app")
 