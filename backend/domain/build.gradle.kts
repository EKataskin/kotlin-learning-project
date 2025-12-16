plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(project(":common"))
    implementation(project(":stubs"))

    // tests
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("test-junit"))
    api(libs.coroutines.test)
}