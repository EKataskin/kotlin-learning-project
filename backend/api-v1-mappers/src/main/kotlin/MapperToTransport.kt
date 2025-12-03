package ru.ekataskin.booktracker.mappers

import ru.ekataskin.booktracker.api.v1.models.*
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.*

fun Context.toTransport(): IResponse = when (val cmd = command) {
    CommandModel.NONE -> throw Exception("Unknown command $cmd")
    CommandModel.CREATE -> toTransportCreate()
    CommandModel.READ -> toTransportRead()
    CommandModel.UPDATE -> toTransportUpdate()
    CommandModel.DELETE -> toTransportDelete()
    CommandModel.SEARCH -> toTransportSearch()
}

fun Context.toTransportCreate() = BookCreateResponse(
    book = bookResponse.toTransport(),
    result = state.toTransport(),
    errors = errors.toTransportErrors()
)

fun Context.toTransportRead() = BookReadResponse(
    book = bookResponse.toTransport(),
    result = state.toTransport(),
    errors = errors.toTransportErrors()
)

fun Context.toTransportUpdate() = BookUpdateResponse(
    book = bookResponse.toTransport(),
    result = state.toTransport(),
    errors = errors.toTransportErrors()
)

fun Context.toTransportDelete() = BookDeleteResponse(
    book = bookResponse.toTransport(),
    result = state.toTransport(),
    errors = errors.toTransportErrors()
)

fun Context.toTransportSearch() = BookSearchResponse(
    books = booksResponse.toTransport(),
    result = state.toTransport(),
    errors = errors.toTransportErrors()
)

fun BookModel.toTransport(): BookResponseObject = BookResponseObject(
    id = id.takeIf { it != BookIdModel.NONE }?.value(),
    author = author.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    series = series.takeIf { !it.isNullOrBlank() },
    seriesNumber = seriesNumber.takeIf { it != null && it > 0 },
    year = year.takeIf { it != null && it > 0 },
    url = url.takeIf { !it.isNullOrBlank() },
    notes = notes.takeIf { !it.isNullOrBlank() },
    dateStart = dateStart.takeIf { !it.isNullOrBlank() },
    dateEnd = dateEnd.takeIf { !it.isNullOrBlank() },
)

private fun StateModel.toTransport(): ResponseResult? = when (this) {
    StateModel.NONE -> null
    StateModel.RUNNING -> ResponseResult.SUCCESS
    StateModel.FAILING -> ResponseResult.ERROR
    StateModel.FINISHING -> ResponseResult.SUCCESS
}

private fun ErrorModel.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun List<ErrorModel>.toTransportErrors(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<BookModel>.toTransport(): List<BookResponseObject>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }


