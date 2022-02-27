import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.mobile.multiplatform.ios-framework")
}

group = "com.itcluster.mobile"
version = "1.0-SNAPSHOT"

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    val ktorVersion = "1.6.1"
    val serializationVersion = "1.2.2"
    val sqlDelightVersion: String by project
    val coroutinesVersion = "1.5.0-native-mt"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation(libs.coroutines)

                api(projects.shared.feature.config)
                api(projects.shared.feature.list)

                api(libs.multiplatformSettings)
                api(libs.napier)
                api(libs.mokoParcelize)
                api(libs.mokoResources)
                api(libs.mokoMvvmCore)
                api(libs.mokoMvvmLiveData)
                api(libs.mokoMvvmState)
                api(libs.mokoUnits)
                api(libs.mokoFields)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.mokoTestCore)
                implementation(libs.mokoMvvmTest)
                implementation(libs.mokoUnitsTest)
                implementation(libs.multiplatformSettingsTest)
                implementation(libs.ktorClientMock)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
                implementation("com.squareup.okhttp3:okhttp:4.9.0")
                implementation(libs.multidex)
                implementation(libs.lifecycleViewModel)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.itcluster.mobile.shared.cache"
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.itcluster.mobile"
}

framework {
    export(projects.shared.domain)
    export(projects.shared.feature.config)
    export(projects.shared.feature.list)

    export(libs.multiplatformSettings)
    export(libs.napier)
    export(libs.mokoParcelize)
    export(libs.mokoResources)
    export(libs.mokoMvvmCore)
    export(libs.mokoMvvmLiveData)
    export(libs.mokoMvvmState)
    export(libs.mokoUnits)
    export(libs.mokoFields)
}