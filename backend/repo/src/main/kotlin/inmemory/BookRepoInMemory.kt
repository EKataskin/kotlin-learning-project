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
) : BookRepoBase(), IBookRepo {
    private val idSequence: AtomicInteger = AtomicInteger(0)
    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<Int, BookEntity>()
        .expireAfterWrite(ttl)
        .build()


    override suspend fun createBook(rq: DbBookRequest): IDbBookResponse = trySingleMethod {
        val id = idSequence.incrementAndGet()
        val book = rq.book.copy(id = BookIdModel(id), lock = getNewLock())
        val entity = BookEntity(book)
        mutex.withLock {
            cache.put(id, entity)
        }
        DbBookResponse(book)
    }

    override suspend fun readBook(rq: DbBookIdRequest): IDbBookResponse = trySingleMethod {
        val id = rq.id.takeIf { it != BookIdModel.NONE }?.value() ?: return@trySingleMethod errorEmptyId
        mutex.withLock {
            cache.get(id)
                ?.let {
                    DbBookResponse(it.toModel())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateBook(rq: DbBookRequest): IDbBookResponse = trySingleMethod {
        val book = rq.book
        val id = book.id.takeIf { it != BookIdModel.NONE } ?: return@trySingleMethod errorEmptyId
        val key = id.value()
        val oldLock = book.lock.takeIf { it != LockModel.NONE } ?: return@trySingleMethod errorEmptyLock(id)

        mutex.withLock {
            val dbBook = cache.get(key)?.toModel()
            when {
                dbBook == null -> errorNotFound(id)
                dbBook.lock == LockModel.NONE -> errorDb(RepoEmptyLockException(id))
                dbBook.lock != oldLock -> errorConcurrency(book, dbBook.lock)
                else -> {
                    val bookToSave = book.copy(lock = getNewLock())
                    val entity = BookEntity(bookToSave)
                    cache.put(key, entity)
                    DbBookResponse(bookToSave)
                }
            }
        }
    }

    override suspend fun deleteBook(rq: DbBookIdRequest): IDbBookResponse = trySingleMethod {
        val id = rq.id.takeIf { it != BookIdModel.NONE } ?: return@trySingleMethod errorEmptyId
        val key = id.value()
        val oldLock = rq.lock.takeIf { it != LockModel.NONE } ?: return@trySingleMethod errorEmptyLock(id)

        mutex.withLock {
            val dbBook = cache.get(key)?.toModel()
            when {
                dbBook == null -> errorNotFound(id)
                dbBook.lock == LockModel.NONE -> errorDb(RepoEmptyLockException(id))
                dbBook.lock != oldLock -> errorConcurrency(dbBook, dbBook.lock)
                else -> {
                    cache.invalidate(key)
                    DbBookResponse(dbBook)
                }
            }
        }
    }

    override suspend fun searchBook(rq: DbBookFilterRequest): IDbBooksResponse = tryMultiMethod {
        val query = rq.filter.searchString.trim().takeIf { it.isNotEmpty() }?.lowercase()
        val result: List<BookModel> = cache.asMap()
            .asSequence()
            .map { it.value.toModel() }
            .filter { book ->
                if (query == null) return@filter true
                val fields: List<String> = listOf(
                    book.title,
                    book.series.orEmpty(),
                    book.author,
                    book.notes.orEmpty()
                ).map { it.trim().lowercase() }

                fields.any { it.contains(query) }
            }
            .toList()
        DbBooksResponse(result)
    } as IDbBooksResponse

    private fun getNewLock(): LockModel = LockModel(NanoId.generate())
}