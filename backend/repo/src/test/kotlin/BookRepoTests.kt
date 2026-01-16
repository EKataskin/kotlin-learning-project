import common.IBookRepoInitializable
import org.junit.jupiter.api.Test
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.BookStateModel
import ru.ekataskin.booktracker.common.repo.DbBookIdRequest
import ru.ekataskin.booktracker.common.repo.DbBookRequest
import ru.ekataskin.booktracker.common.repo.DbBookResponse
import ru.ekataskin.booktracker.common.repo.DbErrorResponse
import ru.ekataskin.booktracker.common.repo.IBookRepo
import kotlin.test.*

abstract class BookRepoCreateTest {
    abstract val repo: IBookRepo

    private val book = BookModel(
        title = "New Book",
        author = "Author Name",
        notes = "A description of the new book.",
        year = 2026
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createBook(DbBookRequest(book))
        assertIs<DbBookResponse>(result)
        assertFalse(result.data.isEmpty())
        assertNotEquals(result.data.id, BookIdModel.NONE)
        assertTrue { result.data.lock.value().isNotEmpty() }
        assertEquals(result.data.title, book.title)
        assertEquals(result.data.author, book.author)
        assertEquals(result.data.notes, book.notes)
        assertEquals(result.data.year, book.year)
        assertNull(result.data.series)
        assertNull(result.data.seriesNumber)
        assertNull(result.data.url)
        assertNull(result.data.dateStart)
        assertNull(result.data.dateEnd)
        assertEquals(result.data.bookState, BookStateModel.PLANNED)
    }
}

open class BookRepoReadTest(
    val repo: IBookRepoInitializable
) {
    val repoItems: List<BookModel>
    val itemReadSuccess: BookModel

    init {
        repoItems = repo.save(
            listOf(
                createTestModel("read1"),
            )
        ).toList()
        itemReadSuccess = repoItems[0]
    }

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readBook(DbBookIdRequest(itemReadSuccess.id))

        assertIs<DbBookResponse>(result)
        assertEquals(result.data, itemReadSuccess)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readBook(DbBookIdRequest(BookIdModel(WRONG_ID)))

        assertIs<DbErrorResponse>(result)
        val error = result.errors.firstOrNull { e -> e.code == "repo-not-found" }
        assertNotNull(error)
        assertEquals(error.field, "id")
    }

    companion object{
        const val WRONG_ID: Int = Int.MIN_VALUE
    }
}