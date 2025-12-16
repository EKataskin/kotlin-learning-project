import kotlinx.coroutines.test.runTest
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.*
import ru.ekataskin.booktracker.domain.Domain
import ru.ekataskin.booktracker.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StubsTest {
    private val domain = Domain()

    @Test
    fun createSuccess() = runTest {
        val ctx = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.SUCCESS,
            bookRequest = Stubs.NEW_BOOK1
        )

        domain.exec(ctx)

        assertEquals(ctx.bookResponse.id, Stubs.BOOK1.id)
        assertEquals(ctx.bookResponse.title, Stubs.BOOK1.title)
        assertEquals(ctx.bookResponse.author, Stubs.BOOK1.author)
    }

    @Test
    fun searchSuccess() = runTest {
        val ctx = Context(
            command = CommandModel.SEARCH,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.SUCCESS,
            bookFilterRequest = BookFilterModel(searchString = "Author")
        )

        domain.exec(ctx)
        val book = ctx.booksResponse.firstOrNull()

        assertTrue { ctx.booksResponse.isNotEmpty() }
        assertTrue { book!=null }
        assertEquals(book?.id, Stubs.BOOK1.id)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = CommandModel.READ,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.BAD_ID,
            id = Stubs.BOOK1.id
        )

        domain.exec(ctx)
        val error = ctx.errors.firstOrNull()

        assertEquals(ctx.bookResponse, BookModel.NONE)
        assertEquals(error?.field, "id")
        assertEquals(error?.group, "validation")
    }

    @Test
    fun badTitle() = runTest {
        val ctx = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.BAD_TITLE,
            bookRequest = Stubs.NEW_BOOK1
        )

        domain.exec(ctx)
        val error = ctx.errors.firstOrNull()

        assertEquals(ctx.bookResponse, BookModel.NONE)
        assertEquals(error?.field, "title")
        assertEquals(error?.group, "validation")
    }

    @Test
    fun notFound() = runTest {
        val ctx = Context(
            command = CommandModel.READ,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.NOT_FOUND,
            id = Stubs.BOOK1.id
        )

        domain.exec(ctx)
        val error = ctx.errors.firstOrNull()

        assertEquals(ctx.bookResponse, BookModel.NONE)
        assertEquals(error?.code, "not found")
        assertEquals(error?.group, "search")
    }

    @Test
    fun unknownCase() = runTest {
        val ctx = Context(
            command = CommandModel.READ,
            environment = EnvironmentModel.STUB,
            stubCase = StubsModel.NONE,
            bookRequest = Stubs.NEW_BOOK1
        )

        domain.exec(ctx)
        val error = ctx.errors.firstOrNull()

        assertEquals(ctx.bookResponse, BookModel.NONE)
        assertEquals(error?.code, "unknown stub")
        assertEquals(error?.group, "validation")
    }
}