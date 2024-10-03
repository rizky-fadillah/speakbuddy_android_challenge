pluginManagement {
    repositories {
        google()
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
rootProject.name = "edison_android_exercise"

include(":app")
include(":data")
include(":database")
include(":network")
include(":model")
include(":domain")
include(":ui")
include(":common")
include(":testing")
include(":designsystem")
include(":feature:randomcat")
include(":feature:facthistory")
