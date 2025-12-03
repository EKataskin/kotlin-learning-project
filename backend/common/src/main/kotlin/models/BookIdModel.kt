package ru.ekataskin.booktracker.common.models

@JvmInline
value class BookIdModel(private val id: Int) {
    fun value() = id

    companion object {
        val NONE = BookIdModel(0)
    }
}
