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
}