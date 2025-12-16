package ru.ekataskin.booktracker.common.models

@JvmInline
value class BookIdModel(private val id: Int) {
    fun value() = id
    fun asString() = id.toString()

    companion object {
        val NONE = BookIdModel(0)
    }
}
