package inmemory

import io.github.reactivecircus.cache4k.Cache
import io.viascom.nanoid.NanoId
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ekataskin.booktracker.common.models.*
import ru.ekataskin.booktracker.common.repo.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BookRepoInMemory(
    ttl: Duration = 10.minutes
) : BookRepoBase(), IBookRepo
{
    private val idSequence: AtomicInteger = AtomicInteger(0)
    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<Int, BookEntity>()
        .expireAfterWrite(ttl)
        .build()


    override suspend fun createBook(rq: DbBookRequest): IDbBookResponse = trySingleMethod {
        val id = idSequence.incrementAndGet()
        val book = rq.book.copy(id = BookIdModel(id), lock = LockModel(NanoId.generate()))
        val entity = BookEntity(book)
        mutex.withLock {
            cache.put(id, entity)
        }
        DbBookResponse(book)
    }

    override suspend fun readBook(rq: DbBookIdRequest): IDbBookResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateBook(rq: DbBookRequest): IDbBookResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBook(rq: DbBookIdRequest): IDbBookResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchBook(rq: DbBookFilterRequest): IDbBooksResponse {
        TODO("Not yet implemented")
    }
}