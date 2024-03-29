pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "GoodJobRay"
include(":app")
include(":MinimalistViews")

// Можно перевести на KTS
// Разобраться с Github Actions / CircleCI -> настроить в своём проекте CI/CD
