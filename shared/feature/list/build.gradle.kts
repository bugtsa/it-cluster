plugins {
    id("multiplatform-library-convention")
    id("kotlin-parcelize")
}

kotlin {

    sourceSets {
        val commonMain by getting {

            dependencies {
                api(projects.shared.domain)
            }
        }

    }
}

dependencies {


    commonMainImplementation(libs.coroutines)

    androidMainImplementation(libs.lifecycleViewModel)
    androidMainImplementation(libs.recyclerView)

    commonMainImplementation(libs.mokoMvvmLiveData)
    commonMainImplementation(libs.mokoMvvmState)
    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoUnits)

    commonMainImplementation(libs.napier)
}
