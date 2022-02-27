buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val kotlinVersion: String by project
    val navigationVersion: String by project
    val sqlDelightVersion: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
        classpath("com.android.tools.build:gradle:7.1.0-rc01")
        classpath(libs.mokoResourcesGeneratorGradle)
        classpath(libs.mokoNetworkGeneratorGradle)
        classpath(libs.mokoUnitsGeneratorGradle)
        classpath(libs.kotlinSerializationGradle)
        classpath(libs.hiltGradle)
        classpath(":build-logic")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}