package ru.ekataskin.booktracker.mappers

import ru.ekataskin.booktracker.api.v1.models.*
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.*

fun Context.fromTransport(request: BookCreateRequest) {
    command = CommandModel.CREATE
    environment = request.mode.toModel()
    stubCase = request.stub.toModel()
    bookRequest = request.book.toModel()
}

fun Context.fromTransport(request: BookReadRequest) {
    command = CommandModel.READ
    environment = request.mode.toModel()
    stubCase = request.stub.toModel()
    bookRequest = request.book.toModel()
}

fun Context.fromTransport(request: BookUpdateRequest) {
    command = CommandModel.UPDATE
    environment = request.mode.toModel()
    stubCase = request.stub.toModel()
    bookRequest = request.book.toModel()
}

fun Context.fromTransport(request: BookDeleteRequest) {
    command = CommandModel.DELETE
    environment = request.mode.toModel()
    stubCase = request.stub.toModel()
    id = request.id.toBookId()
}

fun Context.fromTransport(request: BookSearchRequest) {
    command = CommandModel.SEARCH
    environment = request.mode.toModel()
    stubCase = request.stub.toModel()
    bookFilterRequest = request.bookFilter.toModel()
}

private fun RequestDebugMode?.toModel() = when (this) {
    RequestDebugMode.PROD -> EnvironmentModel.PROD
    RequestDebugMode.TEST -> EnvironmentModel.TEST
    RequestDebugMode.STUB -> EnvironmentModel.STUB
    null -> EnvironmentModel.PROD
}

private fun RequestDebugStubs?.toModel() = when (this) {
    RequestDebugStubs.SUCCESS -> StubsModel.SUCCESS
    RequestDebugStubs.NOT_FOUND -> StubsModel.NOT_FOUND
    RequestDebugStubs.CANT_DELETE -> StubsModel.CANT_DELETE
    RequestDebugStubs.BAD_ID -> StubsModel.BAD_ID
    RequestDebugStubs.BAD_AUTHOR -> StubsModel.BAD_AUTHOR
    RequestDebugStubs.BAD_TITLE -> StubsModel.BAD_TITLE
    RequestDebugStubs.BAD_STATE -> StubsModel.BAD_STATE
    RequestDebugStubs.BAD_URL -> StubsModel.BAD_URL
    RequestDebugStubs.BAD_SEARCH_STRING -> StubsModel.BAD_SEARCH_STRING
    null -> StubsModel.NONE
}

private fun BookCreateObject?.toModel(): BookModel {
    if (this == null) return BookModel()
    return BookModel(
        author = this.author ?: "",
        title = this.title ?: "",
        series = this.series,
        seriesNumber = this.seriesNumber,
        year = this.year,
        url = this.url,
        notes = this.notes,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd
    )
}

private fun BookUpdateObject?.toModel(): BookModel {
    if (this == null) return BookModel()
    return BookModel(
        id = this.id.toBookId(),
        lock = this.lock.toLock(),
        author = this.author ?: "",
        title = this.title ?: "",
        series = this.series,
        seriesNumber = this.seriesNumber,
        year = this.year,
        url = this.url,
        notes = this.notes,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd
    )
}

private fun BookSearchFilter?.toModel(): BookFilterModel {
    if (this == null || this.searchString.isNullOrBlank()) return BookFilterModel()
    return BookFilterModel(searchString = this.searchString!!)
}

private fun Int?.toBookId() = this?.let { BookIdModel(it) } ?: BookIdModel.NONE
private fun String?.toLock() = this?.let { LockModel(it) } ?: LockModel.NONE

private fun BookReadObject?.toModel(): BookModel = if (this != null) {
    BookModel(id = id.toBookId())
} else {
    BookModel()
}
