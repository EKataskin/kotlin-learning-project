package ru.ekataskin.booktracker.common.models

data class BookModel(
    var id: BookIdModel = BookIdModel.NONE,
    var author: String = "",
    var title: String = "",
    var series: String? = null,
    var seriesNumber: Int? = null,
    var year: Int? = null,
    var url: String? = null,
    var notes: String? = null,
    var dateStart: String? = null,
    var dateEnd: String? = null,
    var bookState: BookStateModel? = BookStateModel.PLANNED,
    var lock: LockModel = LockModel.NONE
) {
    fun isEmpty() = this == NONE

    companion object {
        public val NONE = BookModel()
    }
}