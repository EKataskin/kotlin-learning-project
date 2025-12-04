package ru.ekataskin.booktracker.app

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ekataskin.booktracker.api.v1.models.*
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.mappers.fromTransport
import ru.ekataskin.booktracker.mappers.toTransport

@RestController
@RequestMapping("api/v1")
class Controller(
    private val processor: IRequestProcessor
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("create")
    suspend fun create(@RequestBody request: BookCreateRequest): BookCreateResponse =
        processor.process(request, "createBook", log)

    @PostMapping("read")
    suspend fun read(@RequestBody request: BookReadRequest): BookReadResponse =
        processor.process(request, "readBook", log)

    @PostMapping("update")
    suspend fun update(@RequestBody request: BookUpdateRequest): BookUpdateResponse =
        processor.process(request, "updateBook", log)

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: BookDeleteRequest): BookDeleteResponse =
        processor.process(request, "deleteBook", log)

    @PostMapping("search")
    suspend fun  search(@RequestBody request: BookSearchRequest): BookSearchResponse =
        processor.process(request, "searchBook", log)
}

suspend inline fun <reified Q : IRequest, reified S : IResponse> IRequestProcessor.process(
    request: Q,
    logId: String,
    log: KLogger
): S = processRequest(
    { fromTransport(request) },
    { toTransport() as S },
    logId,
    log
)
