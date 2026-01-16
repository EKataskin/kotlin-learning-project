import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import ru.ekataskin.booktracker.common.models.BookModel
import kotlin.time.Duration.Companion.minutes

fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest(timeout = 1.minutes) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}

fun createTestModel(tag: String): BookModel = BookModel(
    title = "Title for $tag",
    author = "Author for $tag",
    notes = tag,
    year = 2000 + tag.length
)