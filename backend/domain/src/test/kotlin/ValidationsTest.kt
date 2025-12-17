import kotlinx.coroutines.test.runTest
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.models.CommandModel
import ru.ekataskin.booktracker.common.models.EnvironmentModel
import ru.ekataskin.booktracker.domain.Domain
import ru.ekataskin.booktracker.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidationsTest {
    private val domain = Domain()

    @Test
    fun validationAuthorTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = Stubs.NEW_BOOK1
        )
        val ctx2 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "   ",
                title = "Some Title",
                url = "https://example.com/book"
            )
        )
        val ctx3 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "&",
                title = "Some Title",
                url = "https://example.com/book"
            )
        )

        domain.exec(ctx1)
        domain.exec(ctx2)
        domain.exec(ctx3)

        assertEquals(ctx1.errors.size, 0)
        val errorAuthorEmpty = ctx2.errors.firstOrNull { e -> e.field == "author" }
        assertTrue(errorAuthorEmpty != null && errorAuthorEmpty.code == "empty" && errorAuthorEmpty.group == "validation")
        val errorAuthorContent = ctx3.errors.firstOrNull { e -> e.field == "author" }
        assertTrue(errorAuthorContent != null && errorAuthorContent.code == "invalid" && errorAuthorContent.group == "validation")
    }
}