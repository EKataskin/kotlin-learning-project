package ru.ekataskin.booktracker.common

import io.github.oshai.kotlinlogging.KLogger
import kotlinx.datetime.Clock
import ru.ekataskin.booktracker.common.interfaces.IDomain
import ru.ekataskin.booktracker.common.interfaces.IRequestProcessor
import ru.ekataskin.booktracker.common.models.ErrorModel
import ru.ekataskin.booktracker.common.models.StateModel

class RequestV1Processor(
    private val domain: IDomain
) : IRequestProcessor {
    override suspend fun <S> processRequest(
        getRequest: suspend Context.() -> Unit,
        toResponse: suspend Context.() -> S,
        logId: String,
        log: KLogger
    ): S {
        val ctx = Context(
            startTime = Clock.System.now(),
        )
        return try {
            ctx.getRequest()
            log.info { "Request $logId started" }
            domain.exec(ctx)
            log.info { "Request $logId processed" }
            ctx.toResponse()
        } catch (e: Throwable) {
            log.error(e) { "Request $logId failed" }
            ctx.state = StateModel.FAILING
            ctx.errors.add(ErrorModel(exception = e))
            ctx.toResponse()
        }
    }
}