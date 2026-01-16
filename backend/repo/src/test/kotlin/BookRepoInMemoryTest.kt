import common.BookRepoInitializable
import inmemory.BookRepoInMemory
import org.junit.jupiter.api.Nested

class BookRepoInMemoryTest {
    @Nested
    inner class BookRepoInMemoryCreateTest : BookRepoCreateTest() {
        override val repo = BookRepoInitializable(
            BookRepoInMemory(),
        )
    }
}