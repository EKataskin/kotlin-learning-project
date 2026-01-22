import common.IBookRepoInitializable
import org.junit.jupiter.api.Test
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.BookStateModel
import ru.ekataskin.booktracker.common.models.LockModel
import ru.ekataskin.booktracker.common.repo.*
import kotlin.test.*

open class BookRepoCreateTest(
    val repo: IBookRepo
) {
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
        val result = repo.readBook(DbBookIdRequest(notFoundId))

        assertIs<DbErrorResponse>(result)
        val error = result.errors.firstOrNull { e -> e.code == "repo-not-found" }
        assertNotNull(error)
        assertEquals(error.field, "id")
    }
}

open class BookRepoUpdateTest(
    val repo: IBookRepoInitializable
) {
    val repoItems: List<BookModel>
    val itemUpdateSuccess: BookModel

    init {
        repoItems = repo.save(
            listOf(
                createTestModel("update1"),
            )
        ).toList()
        itemUpdateSuccess = repoItems[0]
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val updatedTitle = "Changed title"
        val updatedNotes = "Changed notes"
        val updatedDateEnd = "2025-12-31"
        val bookToUpdate = itemUpdateSuccess.copy(
            title = updatedTitle,
            notes = updatedNotes,
            dateEnd = updatedDateEnd,
        )

        val result = repo.updateBook(DbBookRequest(bookToUpdate))

        assertIs<DbBookResponse>(result)
        assertEquals(result.data.id, itemUpdateSuccess.id)
        assertEquals(result.data.title, updatedTitle)
        assertEquals(result.data.notes, updatedNotes)
        assertEquals(result.data.dateEnd, updatedDateEnd)
        assertNotEquals(result.data.lock, itemUpdateSuccess.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val bookToUpdate = createTestModel("update-not-found").apply {
            id = notFoundId
            lock = itemUpdateSuccess.lock
        }

        val result = repo.updateBook(DbBookRequest(bookToUpdate))

        assertIs<DbErrorResponse>(result)
        val error = result.errors.firstOrNull { e -> e.code == "repo-not-found" }
        assertNotNull(error)
        assertEquals(error.field, "id")
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val bookToUpdate = itemUpdateSuccess.copy(
            title = "Changed title with bad lock",
            lock = LockModel("bad-lock-value")
        )

        val result = repo.updateBook(DbBookRequest(bookToUpdate))

        assertIs<DbErrorResponse>(result)
        val error = result.errors.firstOrNull { e -> e.code == "repo-concurrency" }
        assertNotNull(error)
        assertEquals(error.field, "lock")
    }
}