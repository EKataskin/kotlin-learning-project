package ru.ekataskin.booktracker.common.models

@JvmInline
value class LockModel(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = LockModel("")
    }
}