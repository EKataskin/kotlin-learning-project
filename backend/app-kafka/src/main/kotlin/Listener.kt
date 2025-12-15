package ru.ekataskin.booktracker.app.kafka

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
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
    private val processor: IRequestProcessor,
    private val template: KafkaTemplate<String, String>,
    @Value("\${application.kafka.response-topic}") private val responseTopic: String
) {
    private val log = KotlinLogging.logger { }


    @KafkaListener(topics = ["\${application.kafka.topic}"])
    fun listen(record: ConsumerRecord<String, String>) {
        try {
            log.trace { "Processing record [topic=${record.topic()}; partition=${record.partition()}; offset=${record.offset()}; key=${record.key()}]" }
            runBlocking {
                val resp = processor.process(record.value(), "kafka-consumer", log)
                sendResponse(resp)
            }
        } catch (ex: Exception) {
            log.error(ex) { "Error processing record with key=${record.key()}" }
        }
    }

    private suspend fun sendResponse(json: String) {
        try {
            template.send(responseTopic, json).await()
            log.info { "Message sent to $responseTopic" }
        }
        catch (ex: Exception) {
            log.error(ex) { "Error sending response to topic $responseTopic" }
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
