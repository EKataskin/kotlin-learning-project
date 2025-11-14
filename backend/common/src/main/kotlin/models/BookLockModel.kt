package ru.ekataskin.booktracker.common.models

@JvmInline
value class BookLockModel(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = BookLockModel("")
    }
}