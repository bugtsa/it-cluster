plugins {
    kotlin("plugin.serialization")
    id("multiplatform-library-convention")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dev.icerock.mobile.multiplatform-network-generator")
}

dependencies {
    commonMainImplementation(libs.coroutines)
    commonMainImplementation(libs.kotlinSerialization)
    commonMainImplementation(libs.ktorClient)
    commonMainImplementation(libs.ktorClientLogging)

    commonMainImplementation(libs.mokoParcelize)
    commonMainImplementation(libs.mokoNetwork)

    commonMainImplementation(libs.multiplatformSettings)
    commonMainImplementation(libs.napier)
}

mokoNetwork {
    spec("news") {
        inputSpec = file("src/openapi.yml")
    }
}

kotlin {

    val ktorVersion = "1.6.1"
    val serializationVersion = "1.2.2"
    val coroutinesVersion = "1.5.0-native-mt"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation(libs.coroutines)

//                api(libs.multiplatformSettings)
//                api(libs.napier)
//                api(libs.mokoParcelize)
//                api(libs.mokoResources)
//                api(libs.mokoMvvmCore)
//                api(libs.mokoMvvmLiveData)
//                api(libs.mokoMvvmState)
//                api(libs.mokoUnits)
//                api(libs.mokoFields)
            }
        }
    }
}
