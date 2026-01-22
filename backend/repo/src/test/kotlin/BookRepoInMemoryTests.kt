import inmemory.BookRepoInMemory
import org.junit.jupiter.api.Nested

class BookRepoInMemoryTests {
    @Nested
    inner class BookRepoInMemoryCreateTest : BookRepoCreateTest(
        repo = BookRepoInMemory()
    )

    @Nested
    inner class BookRepoInMemoryReadTest : BookRepoReadTest(
        repo = BookRepoInMemory()
    )

    @Nested
    inner class BookRepoInMemoryUpdateTest : BookRepoUpdateTest(
        repo = BookRepoInMemory()
    )

    @Nested
    inner class BookRepoInMemoryDeleteTest : BookRepoDeleteTest(
        repo = BookRepoInMemory()
    )

    @Nested
    inner class BookRepoInMemorySearchTest : BookRepoSearchTest(
        repo = BookRepoInMemory()
    )
}