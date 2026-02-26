package ru.ekataskin.booktracker.common.repo

interface IBookRepo {
    suspend fun createBook(rq: DbBookRequest): IDbBookResponse
    suspend fun readBook(rq: DbBookIdRequest): IDbBookResponse
    suspend fun updateBook(rq: DbBookRequest): IDbBookResponse
    suspend fun deleteBook(rq: DbBookIdRequest): IDbBookResponse
    suspend fun searchBook(rq: DbBookFilterRequest): IDbBooksResponse

    companion object {
        val NONE = object : IBookRepo {
            override suspend fun createBook(rq: DbBookRequest): IDbBookResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readBook(rq: DbBookIdRequest): IDbBookResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateBook(rq: DbBookRequest): IDbBookResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteBook(rq: DbBookIdRequest): IDbBookResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchBook(rq: DbBookFilterRequest): IDbBooksResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}