package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.ErrorModel
import ru.ekataskin.booktracker.common.models.LockModel
import ru.ekataskin.booktracker.common.systemError

const val ERROR_GROUP_REPO = "repo"

val errorEmptyId = DbErrorResponse(
    ErrorModel(
        code = "${ERROR_GROUP_REPO}-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorNotFound(id: BookIdModel) = DbErrorResponse(
    ErrorModel(
        code = "${ERROR_GROUP_REPO}-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.value()} is not Found",
    )
)

fun errorEmptyLock(id: BookIdModel) = DbErrorResponse(
    ErrorModel(
        code = "${ERROR_GROUP_REPO}-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.value()} is empty that is not admitted"
    )
)

fun errorConcurrency(
    book: BookModel,
    actualLock: LockModel,
    exception: Exception = RepoConcurrencyException(
        id = book.id,
        expectedLock = book.lock,
        actualLock = actualLock,
    ),
) = DbErrorResponse(
    ErrorModel(
        code = "${ERROR_GROUP_REPO}-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${book.id.value()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorDb(e: Throwable) = DbErrorResponse(
    systemError(
        code = "dbLockEmpty",
        e = e
    )
)