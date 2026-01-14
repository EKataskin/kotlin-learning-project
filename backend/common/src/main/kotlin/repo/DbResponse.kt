package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.models.BookModel

sealed interface IDbBookResponse: IDbResponse<BookModel>
sealed interface IDbBooksResponse: IDbResponse<List<BookModel>>

data class DbBookResponseOk(
    val data: BookModel
): IDbBookResponse