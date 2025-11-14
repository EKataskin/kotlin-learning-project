package ru.ekataskin.booktracker.common.models

@JvmInline
value class BookLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BookLock("")
    }
}