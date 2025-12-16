package ru.ekataskin.booktracker.stubs

import ru.ekataskin.booktracker.api.v1.models.BookCreateObject
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.BookModel

object Stubs {
    val NEW_BOOK1 = BookModel(
        author = "Александр Пушкин",
        title = "Евгений Онегин",
        url = "https://ru.wikipedia.org/wiki/Евгений_Онегин",
    )
    val BOOK1 = BookModel(
        id = BookIdModel(37),
        author = "Александр Пушкин",
        title = "Евгений Онегин",
        url = "https://ru.wikipedia.org/wiki/Евгений_Онегин",
    )
    val BOOK2 = BookModel(
        id = BookIdModel(42),
        author = "Михаил Лермонтов",
        title = "Герой нашего времени",
        url = "https://ru.wikipedia.org/wiki/Герой_нашего_времени",
    )

    val BOOK_CREATE_OBJECT1 = BookCreateObject(
        author = "Александр Пушкин",
        title = "Евгений Онегин",
        url = "https://ru.wikipedia.org/wiki/Евгений_Онегин",
    )
}