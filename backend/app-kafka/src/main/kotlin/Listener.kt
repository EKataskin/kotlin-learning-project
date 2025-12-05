package ru.ekataskin.booktracker.app.kafka

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.ekataskin.booktracker.api.v1.apiV1RequestDeserialize
import ru.ekataskin.booktracker.api.v1.apiV1ResponseSerialize
import ru.ekataskin.booktracker.api.v1.models.IRequest
import ru.ekataskin.booktracker.api.v1.models.IResponse
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.mappers.fromTransport
import ru.ekataskin.booktracker.mappers.toTransport

@Component
class Listener(
    private val processor: IRequestProcessor
) {
    private val log = KotlinLogging.logger { }

    @KafkaListener(topics = ["\${application.kafka.topic}"])
    fun listen(record: ConsumerRecord<String, String>) {
        log.trace { "Processing record [topic=${record.topic()}; partition=${record.partition()}; offset=${record.offset()}; key=${record.key()}]" }
        try {
            runBlocking {
                processor.process(record.value(), "kafka-consumer", log)
            }
        } catch (ex: Exception) {
            log.error(ex) { "Error processing record with key=${record.key()}" }
        }
    }
}

suspend inline fun IRequestProcessor.process(
    value: String,
    logId: String,
    log: KLogger
): String = processRequest(
    {
        val request: IRequest = apiV1RequestDeserialize(value)
        fromTransport(request)
    },
    {
        val response: IResponse = toTransport()
        apiV1ResponseSerialize(response)
    },
    logId,
    log
)
