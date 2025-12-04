package ru.ekataskin.booktracker.domain

import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.interfaces.IDomain
import ru.ekataskin.booktracker.common.models.*

class Domain : IDomain {
    override suspend fun exec(ctx: Context) {
        ctx.bookResponse = BookModel(
            id = BookIdModel(1),
            author = "Автор",
            title = "Название",
            url = "https://url.com",
        )
        ctx.booksResponse = mutableListOf()
        ctx.state = StateModel.FINISHING
    }
}