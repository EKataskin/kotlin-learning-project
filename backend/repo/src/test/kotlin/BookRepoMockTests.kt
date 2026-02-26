import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import ru.ekataskin.booktracker.common.models.BookFilterModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.repo.*
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BookRepoMockTests {
    private val book = createTestModel("mock")
    private val repo = BookRepositoryMock(
        invokeCreateBook = { DbBookResponse(book.copy().apply { title = "create" }) },
        invokeReadBook = { DbBookResponse(book.copy().apply { title = "read" }) },
        invokeUpdateBook = { DbBookResponse(book.copy().apply { title = "update" }) },
        invokeDeleteBook = { DbBookResponse(book.copy().apply { title = "delete" }) },
        invokeSearchBook = { DbBooksResponse(listOf(book.copy().apply { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createBook(DbBookRequest(BookModel()))
        assertIs<DbBookResponse>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readBook(DbBookIdRequest(BookModel()))
        assertIs<DbBookResponse>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateBook(DbBookRequest(BookModel()))
        assertIs<DbBookResponse>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteBook(DbBookIdRequest(BookModel()))
        assertIs<DbBookResponse>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchBook(DbBookFilterRequest(BookFilterModel()))
        assertIs<DbBooksResponse>(result)
        assertEquals("search", result.data.first().title)
    }
}

class BookRepositoryMock(
    private val invokeCreateBook: (DbBookRequest) -> IDbBookResponse = { DEFAULT_BOOK_SUCCESS_EMPTY_MOCK },
    private val invokeReadBook: (DbBookIdRequest) -> IDbBookResponse = { DEFAULT_BOOK_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateBook: (DbBookRequest) -> IDbBookResponse = { DEFAULT_BOOK_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteBook: (DbBookIdRequest) -> IDbBookResponse = { DEFAULT_BOOK_SUCCESS_EMPTY_MOCK },
    private val invokeSearchBook: (DbBookFilterRequest) -> IDbBooksResponse = { DEFAULT_BOOKS_SUCCESS_EMPTY_MOCK },
) : IBookRepo {
    override suspend fun createBook(rq: DbBookRequest): IDbBookResponse {
        return invokeCreateBook(rq)
    }

    override suspend fun readBook(rq: DbBookIdRequest): IDbBookResponse {
        return invokeReadBook(rq)
    }

    override suspend fun updateBook(rq: DbBookRequest): IDbBookResponse {
        return invokeUpdateBook(rq)
    }

    override suspend fun deleteBook(rq: DbBookIdRequest): IDbBookResponse {
        return invokeDeleteBook(rq)
    }

    override suspend fun searchBook(rq: DbBookFilterRequest): IDbBooksResponse {
        return invokeSearchBook(rq)
    }

    companion object {
        val DEFAULT_BOOK_SUCCESS_EMPTY_MOCK = DbBookResponse(BookModel())
        val DEFAULT_BOOKS_SUCCESS_EMPTY_MOCK = DbBooksResponse(emptyList())
    }
}