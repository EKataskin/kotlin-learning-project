plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":stubs"))
}