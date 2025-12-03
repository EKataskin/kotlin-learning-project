plugins {
    alias { libs.plugins.kotlin.jvm }
    alias { libs.plugins.spring.boot }
    alias { libs.plugins.spring.dependencies }
    alias { libs.plugins.spring.kotlin }
    alias { libs.plugins.kotlinx.serialization }
    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))

    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Модели
    implementation(project(":common"))

    // v1 api
    implementation(project(":api-v1-jackson"))
    implementation(project(":api-v1-mappers"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "ru.ekataskin.app.AppKt"
}
