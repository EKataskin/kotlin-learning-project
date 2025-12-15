package ru.ekataskin.booktracker.domain

import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.interfaces.IDomain
import ru.ekataskin.booktracker.common.models.*
import ru.ekataskin.booktracker.stubs.Stubs

class Domain : IDomain {
    override suspend fun exec(ctx: Context) {
        ctx.bookResponse = Stubs.BOOK1
        ctx.booksResponse = mutableListOf()
        ctx.state = StateModel.FINISHING
    }
}