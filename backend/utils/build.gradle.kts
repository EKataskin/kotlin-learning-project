plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`../../gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlinx)
    testImplementation(kotlin("test-junit5"))
}