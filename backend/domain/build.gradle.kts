plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(project(":common"))
    implementation(project(":stubs"))
}