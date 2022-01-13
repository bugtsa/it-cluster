enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()

        jcenter {
            content {
                includeGroup("org.jetbrains.kotlinx")
            }
        }
    }
}

rootProject.name = "ItCluster"

include(":androidApp")
include(":shared")
include(":shared:domain")
include(":shared:feature:config")
include(":shared:feature:list")

includeBuild("build-logic")