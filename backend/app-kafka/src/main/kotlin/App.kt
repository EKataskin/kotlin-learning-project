package ru.ekataskin.booktracker.app.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class App {
    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        log.info { "Application (kafka) is ready" }
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
