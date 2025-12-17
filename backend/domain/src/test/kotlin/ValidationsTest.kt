import kotlinx.coroutines.test.runTest
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.BookFilterModel
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

    @Test
    fun validationTitleTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = Stubs.NEW_BOOK1
        )
        val ctx2 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "   ",
                url = "https://example.com/book"
            )
        )
        val ctx3 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "!!!",
                url = "https://example.com/book"
            )
        )

        domain.exec(ctx1)
        domain.exec(ctx2)
        domain.exec(ctx3)

        assertEquals(ctx1.errors.size, 0)
        val errorTitleEmpty = ctx2.errors.firstOrNull { e -> e.field == "title" }
        assertTrue(errorTitleEmpty != null && errorTitleEmpty.code == "empty" && errorTitleEmpty.group == "validation")
        val errorTitleContent = ctx3.errors.firstOrNull { e -> e.field == "title" }
        assertTrue(errorTitleContent != null && errorTitleContent.code == "invalid" && errorTitleContent.group == "validation")
    }

    @Test
    fun validationUrlTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = Stubs.NEW_BOOK1
        )
        val ctx2 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "invalid_url"
            )
        )

        domain.exec(ctx1)
        domain.exec(ctx2)

        assertEquals(ctx1.errors.size, 0)
        val errorUrlInvalid = ctx2.errors.firstOrNull { e -> e.field == "url" }
        assertTrue(errorUrlInvalid != null && errorUrlInvalid.code == "invalid" && errorUrlInvalid.group == "validation")
    }

    @Test
    fun validationYearTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = Stubs.NEW_BOOK1
        )
        val ctx2 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                year = 3000
            )
        )

        domain.exec(ctx1)
        domain.exec(ctx2)

        assertEquals(ctx1.errors.size, 0)
        val errorYearInvalid = ctx2.errors.firstOrNull { e -> e.field == "year" }
        assertTrue(errorYearInvalid != null && errorYearInvalid.code == "invalid" && errorYearInvalid.group == "validation")
    }

    @Test
    fun validationSeriesPairTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = Stubs.NEW_BOOK1
        )
        val ctx2 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                series = "Some Series"
            )
        )
        val ctx3 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                seriesNumber = 1
            )
        )

        domain.exec(ctx1)
        domain.exec(ctx2)
        domain.exec(ctx3)

        assertEquals(ctx1.errors.size, 0)
        val errorSeriesPair1 = ctx2.errors.firstOrNull { e -> e.field == "series/seriesNumber" }
        assertTrue(errorSeriesPair1 != null && errorSeriesPair1.code == "invalid" && errorSeriesPair1.group == "validation")
        val errorSeriesPair2 = ctx3.errors.firstOrNull { e -> e.field == "series/seriesNumber" }
        assertTrue(errorSeriesPair2 != null && errorSeriesPair2.code == "invalid" && errorSeriesPair2.group == "validation")
    }

    @Test
    fun validationSeriesTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                series = "!!!",
                seriesNumber = 1
            )
        )

        domain.exec(ctx1)

        val errorSeriesContent = ctx1.errors.firstOrNull { e -> e.field == "series" }
        assertTrue(errorSeriesContent != null && errorSeriesContent.code == "invalid" && errorSeriesContent.group == "validation")
    }

    @Test
    fun validationSeriesNumberTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                series = "Some Series",
                seriesNumber = 0
            )
        )

        domain.exec(ctx1)

        val errorSeriesNumberInvalid = ctx1.errors.firstOrNull { e -> e.field == "seriesNumber" }
        assertTrue(errorSeriesNumberInvalid != null && errorSeriesNumberInvalid.code == "invalid" && errorSeriesNumberInvalid.group == "validation")
    }

    @Test
    fun validationAllValidTest() = runTest {
        val ctx = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                year = 2020,
                series = "Some Series",
                seriesNumber = 1
            )
        )

        domain.exec(ctx)

        assertEquals(ctx.errors.size, 0)
    }

    @Test
    fun validationDateStartTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                dateStart = "январь 2020"
            )
        )

        domain.exec(ctx1)

        val errorDateStartInvalid = ctx1.errors.firstOrNull { e -> e.field == "dateStart" }
        assertTrue(errorDateStartInvalid != null && errorDateStartInvalid.code == "invalid" && errorDateStartInvalid.group == "validation")
    }

    @Test
    fun validationDateEndTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                dateEnd = "12-12-2025"
            )
        )

        domain.exec(ctx1)

        val errorDateEndInvalid = ctx1.errors.firstOrNull { e -> e.field == "dateEnd" }
        assertTrue(errorDateEndInvalid != null && errorDateEndInvalid.code == "invalid" && errorDateEndInvalid.group == "validation")
    }

    @Test
    fun validationDatesOrderTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.CREATE,
            environment = EnvironmentModel.TEST,
            bookRequest = BookModel(
                author = "Some Author",
                title = "Some Title",
                url = "https://example.com/book",
                dateStart = "2022-01-01",
                dateEnd = "2021-01-01"
            )
        )

        domain.exec(ctx1)

        val errorDatesOrderInvalid = ctx1.errors.firstOrNull { e -> e.field == "dateStart/dateEnd" }
        assertTrue(errorDatesOrderInvalid != null && errorDatesOrderInvalid.code == "invalid" && errorDatesOrderInvalid.group == "validation")
    }

    @Test
    fun validationSearchStringTest() = runTest {
        val ctx1 = Context(
            command = CommandModel.SEARCH,
            environment = EnvironmentModel.TEST,
            bookFilterRequest = BookFilterModel(
                searchString = "???"
            )
        )

        domain.exec(ctx1)

        val errorSearchStringEmpty = ctx1.errors.firstOrNull { e -> e.field == "searchString" }
        assertTrue(errorSearchStringEmpty != null && errorSearchStringEmpty.code == "invalid" && errorSearchStringEmpty.group == "validation")
    }
}