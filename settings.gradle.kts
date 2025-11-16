pluginManagement {
    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
//        mavenCentral()
//        gradlePluginPortal()
        maven { url = uri("https://srepo.tosantechno.net/repository/maven-group/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        google()
//        mavenCentral()
        maven { url = uri("https://srepo.tosantechno.net/repository/maven-group/") }
    }
}

rootProject.name = "ComposeWebRtc"
include(":app")
 