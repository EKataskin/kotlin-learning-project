package ru.ekataskin.booktracker.app.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.kafka.annotation.KafkaListener

@Component
class Listener {
    private val log = KotlinLogging.logger { }

    @KafkaListener(topics = ["\${application.kafka.topic}"])
    fun listen() {
        log.info { "Listening to Kafka topics..." }
    }
}