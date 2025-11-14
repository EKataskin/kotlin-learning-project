package ru.ekataskin.booktracker.common.models

data class Book(
    var id: BookId = BookId.NONE,
    var author: String = "",
    var title: String = "",
    var series: String? = null,
    var seriesNumber: Int? = null,
    var year: Int? = null,
    var url: String? = null,
    var notes: String? = null,
    var dateStart: String? = null,
    var dateEnd: String? = null,
    var bookState: BookState? = BookState.PLANNED,
    var lock: BookLock = BookLock.NONE
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Book()
    }
}