plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.nanoid)
    implementation(libs.db.cache4k)

    // tests
    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit5)
    testRuntimeOnly(libs.junit5)
    api(libs.coroutines.test)

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}