plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":api-v1-jackson"))
}