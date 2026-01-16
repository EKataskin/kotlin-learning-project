import common.IBookRepoInitializable
import org.junit.jupiter.api.Test
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.BookStateModel
import ru.ekataskin.booktracker.common.repo.DbBookRequest
import ru.ekataskin.booktracker.common.repo.DbBookResponse
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

abstract class BookRepoCreateTest {
    abstract val repo: IBookRepoInitializable

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