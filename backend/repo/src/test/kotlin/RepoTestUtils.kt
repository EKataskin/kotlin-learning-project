import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel
import kotlin.time.Duration.Companion.minutes

fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest(timeout = 1.minutes) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}

fun createTestModel(
    tag: String,
    author: String = "Author for $tag",
    title: String = "Title for $tag",
    notes: String = "Notes for $tag",
) = BookModel(
    author = author,
    title = title,
    notes = notes,
    year = 2000 + tag.length
)

const val WRONG_ID: Int = Int.MIN_VALUE

val notFoundId = BookIdModel(WRONG_ID)