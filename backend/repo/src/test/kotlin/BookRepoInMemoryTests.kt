import inmemory.BookRepoInMemory
import org.junit.jupiter.api.Nested

class BookRepoInMemoryTests {
    @Nested
    inner class BookRepoInMemoryCreateTest : BookRepoCreateTest() {
        override val repo = BookRepoInMemory()
    }

    @Nested
    inner class BookRepoInMemoryReadTest : BookRepoReadTest(
        repo = BookRepoInMemory()
    )
}