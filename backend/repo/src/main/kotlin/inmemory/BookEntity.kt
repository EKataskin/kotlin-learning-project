package inmemory

import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.BookStateModel
import ru.ekataskin.booktracker.common.models.LockModel

data class BookEntity(
    var id: Int? = null,
    var author: String? = null,
    var title: String? = null,
    var series: String? = null,
    var seriesNumber: Int? = null,
    var year: Int? = null,
    var url: String? = null,
    var notes: String? = null,
    var dateStart: String? = null,
    var dateEnd: String? = null,
    var bookState: String? = null,
    var lock: String? = null
) {
    constructor(book: BookModel) : this(
        id = book.id.value(),
        author = book.author.takeIf { it.isNotBlank() },
        title = book.title.takeIf { it.isNotBlank() },
        series = book.series,
        seriesNumber = book.seriesNumber,
        year = book.year,
        url = book.url,
        notes = book.notes,
        dateStart = book.dateStart,
        dateEnd = book.dateEnd,
        bookState = book.bookState?.name,
        lock = book.lock.value().takeIf { it.isNotBlank() }
    )

    fun toModel() = BookModel(
        id = id?.let { BookIdModel(it) } ?: BookIdModel.NONE,
        author = author ?: "",
        title = title ?: "",
        series = series,
        seriesNumber = seriesNumber,
        year = year,
        url = url,
        notes = notes,
        dateStart = dateStart,
        dateEnd = dateEnd,
        bookState = bookState?.let { BookStateModel.valueOf(it) } ?: BookStateModel.PLANNED,
        lock = lock?.let { LockModel(it) } ?: LockModel.NONE
    )
}