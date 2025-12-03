package ru.ekataskin.booktracker.common.interfaces

import ru.ekataskin.booktracker.common.Context

interface IDomain {
    suspend fun exec(ctx: Context)
}