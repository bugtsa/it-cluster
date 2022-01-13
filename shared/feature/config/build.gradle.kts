plugins {
    id("multiplatform-library-convention")
}

dependencies {
    commonMainImplementation(libs.coroutines)

    androidMainImplementation(libs.lifecycleViewModel)

    commonMainImplementation(libs.mokoMvvmLiveData)
    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoFields)
}
