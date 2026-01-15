plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":api-v1-jackson"))
    implementation(project(":stubs"))

    testImplementation(kotlin("test-junit5"))
}
