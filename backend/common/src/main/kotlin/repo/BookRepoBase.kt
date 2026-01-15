package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.systemError

abstract class BookRepoBase: IBookRepo {
    protected suspend fun trySingleMethod(block: suspend () -> IDbBookResponse) = try {
        block()
    } catch (e: Throwable) {
        DbErrorResponse(systemError("methodException", e = e))
    }

    protected suspend fun tryMultiMethod(block: suspend () -> IDbBooksResponse) = try {
        block()
    } catch (e: Throwable) {
        DbErrorResponse(systemError("methodException", e = e))
    }
}