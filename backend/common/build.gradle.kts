plugins {
    alias { libs.plugins.kotlin.jvm }
    alias { libs.plugins.kotlinx.serialization }
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.logging)

    // tests
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("test-junit"))
    api(libs.coroutines.test)
}