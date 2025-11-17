package ru.ekataskin.booktracker.mappers

import ru.ekataskin.booktracker.api.v1.models.BookCreateObject
import ru.ekataskin.booktracker.api.v1.models.BookState
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.BookStateModel

fun BookModel.toCreateRequest() = BookCreateObject(
    author = author,
    title = title,
    series = series,
    seriesNumber = seriesNumber,
    year = year,
    url = url,
    notes = notes,
    dateStart = dateStart,
    dateEnd = dateEnd,
    bookState = bookState.toRequest(),
)

private fun BookStateModel?.toRequest() = when (this) {
    BookStateModel.PLANNED -> BookState.PLANNED
    BookStateModel.READING -> BookState.READING
    BookStateModel.READ -> BookState.READ
    null -> BookState.PLANNED
}