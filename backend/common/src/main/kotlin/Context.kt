package ru.ekataskin.booktracker.common

import kotlinx.datetime.Instant
import ru.ekataskin.booktracker.common.models.*

data class Context(
    var command: CommandModel = CommandModel.NONE,
    var state: StateModel = StateModel.NONE,
    val errors: MutableList<ErrorModel> = mutableListOf(),
    var environment: EnvironmentModel = EnvironmentModel.PROD,
    var stubCase: StubsModel = StubsModel.NONE,
    var requestId: RequestIdModel = RequestIdModel.NONE,
    var startTime: Instant = Instant.NONE,

    var bookRequest: BookModel = BookModel(),
    var bookFilterRequest: BookFilterModel = BookFilterModel(),
    var bookResponse: BookModel = BookModel(),
    var booksResponse: MutableList<BookModel> = mutableListOf(),
)