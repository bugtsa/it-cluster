plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dev.icerock.mobile.multiplatform-units")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

dependencies {

    val navigationVersion: String by project
    val lifecycleVersion = "2.3.1"
    val fragmentsKtxVersion = "1.3.4"


    implementation(project(":shared"))
    implementation(libs.appCompat)
    implementation(libs.material)
    implementation(libs.recyclerView)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.mokoMvvmDataBinding)
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.cardview:cardview:1.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:$navigationVersion")
    implementation("androidx.navigation:navigation-ui:$navigationVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation( "androidx.fragment:fragment-ktx:$fragmentsKtxVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("androidx.core:core-ktx:1.3.1")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
}

android {
    buildFeatures.dataBinding = true

    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.itcluster.mobile.androidApp"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        val url = "https://newsapi.org/v2/"
        buildConfigField("String", "BASE_URL", "\"$url\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

kapt {
    javacOptions {
        // These options are normally set automatically via the Hilt Gradle plugin, but we
        // set them manually to workaround a bug in the Kotlin 1.5.20
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

multiplatformUnits {
    classesPackage = "com.itcluster.mobile.app"
    dataBindingPackage = "com.itcluster.mobile.app"
    layoutsSourceSet = "main"
}

