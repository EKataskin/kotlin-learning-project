package ru.ekataskin.booktracker.common.models

@JvmInline
value class BookId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BookId("")
    }
}
