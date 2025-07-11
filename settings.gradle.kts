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
    }
}

rootProject.name = "ShowMeTheMoney"
include(":app")
include(":domain")
include(":data")
include(":feature")
include(":feature:settings")
include(":core")
include(":core:ui")
include(":feature:category")
include(":feature:account")
include(":feature:transactions")
