package common

import ru.ekataskin.booktracker.common.models.BookModel
import ru.ekataskin.booktracker.common.repo.IBookRepo

interface IBookRepoInitializable: IBookRepo {
    fun save(items: Collection<BookModel>): Collection<BookModel>
}

class BookRepoInitializable(
    val repo: IBookRepoInitializable,
    items: Collection<BookModel> = emptyList(),
) : IBookRepoInitializable by repo {
    val objects = save(items).toList()
}