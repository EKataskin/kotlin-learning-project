package ru.ekataskin.booktracker.app

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*
import ru.ekataskin.booktracker.api.v1.models.BookCreateRequest
import ru.ekataskin.booktracker.api.v1.models.BookCreateResponse
import ru.ekataskin.booktracker.api.v1.models.IRequest
import ru.ekataskin.booktracker.api.v1.models.IResponse
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.mappers.fromTransport
import ru.ekataskin.booktracker.mappers.toTransport

@RestController
@RequestMapping("api/v1")
class Controller(
    private val processor: IRequestProcessor
) {
    private val log = KotlinLogging.logger {}

    suspend fun create(@RequestBody request: BookCreateRequest): BookCreateResponse {
        return process(processor, request, "createBook", log)
    }

    companion object {
        suspend inline fun <reified Q : IRequest, reified S : IResponse> process(
            processor: IRequestProcessor,
            request: Q,
            logId: String,
            log: KLogger
        ): S = processor.processRequest(
            { fromTransport(request) },
            { toTransport() as S },
            logId,
            log
        )
    }
}