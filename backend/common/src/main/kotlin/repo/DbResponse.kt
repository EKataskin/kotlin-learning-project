package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.ErrorModel

sealed interface IDbBookResponse: IDbResponse<BookModel>
sealed interface IDbBooksResponse: IDbResponse<List<BookModel>>

data class DbBookResponse(
    val data: BookModel
): IDbBookResponse

data class DbBooksResponse(
    val data: List<BookModel>
): IDbBooksResponse

data class DbErrorResponse(
    val errors: List<ErrorModel> = emptyList()
): IDbBookResponse {
    constructor(err: ErrorModel): this(listOf(err))
}