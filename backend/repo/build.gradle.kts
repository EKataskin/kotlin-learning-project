plugins {
    alias { libs.plugins.kotlin.jvm }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.nanoid)
    implementation(libs.db.cache4k)
}