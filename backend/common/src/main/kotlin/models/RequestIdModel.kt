package ru.ekataskin.booktracker.common.models

@JvmInline
value class RequestIdModel(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RequestIdModel("")
    }
}