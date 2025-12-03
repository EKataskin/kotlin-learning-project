import org.junit.Test
import ru.ekataskin.booktracker.api.v1.models.*
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.models.*
import ru.ekataskin.booktracker.mappers.*
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = BookCreateRequest(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS,
            book = Stubs.NEW_BOOK1.toCreateRequest()
        )

        val context = Context()
        context.fromTransport(req)

        assertEquals(context.stubCase, StubsModel.SUCCESS)
        assertEquals(context.environment, EnvironmentModel.STUB)
        assertEquals(context.bookRequest, Stubs.NEW_BOOK1)
    }

    @Test
    fun toTransport() {
        val context = Context(
            requestId = RequestIdModel("1234"),
            command = CommandModel.CREATE,
            bookResponse = Stubs.BOOK1,
            errors = mutableListOf(
                ErrorModel(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = StateModel.RUNNING,
        )

        val resp = context.toTransport() as BookCreateResponse

        assertEquals(resp.book, Stubs.BOOK1.toTransport())
        assertEquals(1, resp.errors?.size)
        assertEquals("err", resp.errors?.firstOrNull()?.code)
    }
}