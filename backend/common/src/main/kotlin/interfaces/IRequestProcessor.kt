package ru.ekataskin.booktracker.common.interfaces

import io.github.oshai.kotlinlogging.KLogger
import ru.ekataskin.booktracker.common.Context

interface IRequestProcessor {
    suspend fun <S> processRequest(
        getRequest: suspend Context.() -> Unit,
        toResponse: suspend Context.() -> S,
        logId: String,
        log: KLogger
    ): S
}