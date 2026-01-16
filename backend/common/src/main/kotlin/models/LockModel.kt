package ru.ekataskin.booktracker.common.models

@JvmInline
value class LockModel(private val id: String) {
    fun value() = id

    companion object {
        val NONE = LockModel("")
    }
}