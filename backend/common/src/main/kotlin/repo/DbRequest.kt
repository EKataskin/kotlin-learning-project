package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.models.BookFilterModel
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.LockModel

data class DbBookRequest(
    val book: BookModel
)

data class DbBookIdRequest(
    val id: BookIdModel,
    val lock: LockModel = LockModel.NONE,
) {
    constructor(book: BookModel) : this(book.id, book.lock)
}

data class DbBookFilterRequest(
    val filter: BookFilterModel
)