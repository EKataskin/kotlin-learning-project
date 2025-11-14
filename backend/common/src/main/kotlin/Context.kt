package ru.ekataskin.booktracker.common

import kotlinx.datetime.Instant
import ru.ekataskin.booktracker.common.models.*

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<Error> = mutableListOf(),
    var environment: Environment = Environment.PROD,
    var stubCase: Stubs = Stubs.NONE,
    var requestId: RequestId = RequestId.NONE,
    var startTime: Instant = Instant.NONE,

    var bookRequest: Book = Book(),
    var bookFilterRequest: BookFilter = BookFilter(),
    var bookResponse: Book = Book(),
    var booksResponse: MutableList<Book> = mutableListOf(),
)