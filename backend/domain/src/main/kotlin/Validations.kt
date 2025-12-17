package ru.ekataskin.booktracker.domain

import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.cor.ICorChainDsl
import ru.ekataskin.booktracker.common.cor.worker
import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.ErrorModel
import ru.ekataskin.booktracker.common.models.StateModel
import java.util.regex.Pattern

fun ICorChainDsl<Context>.collectValidationErrors(title: String) = worker {
    this.title = title
    this.description = "Переводит состояние контекста в FAILING, если были накоплены ошибки"
    on { state == StateModel.RUNNING && errors.isNotEmpty() }
    handle {
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.validateId(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле id содержит валидный идентификатор"
    on { state == StateModel.RUNNING && bookRequest.id != BookIdModel.NONE }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "empty",
                field = "id",
                message = "Field id must not be empty"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateAuthor(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле author не пустое и содержит буквы"
    val regExp = Regex("\\p{L}")
    on { state == StateModel.RUNNING && (bookRequest.author.isBlank() || !bookRequest.author.contains(regExp)) }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = if(bookRequest.author.isBlank()) "empty" else "invalid",
                field = "author",
                message = "Field author must not be empty and must contain letters"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateTitle(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле title не пустое и содержит что-то осмысленное"
    on { state == StateModel.RUNNING && (bookRequest.title.isBlank() || !bookRequest.title.validateContent()) }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = if(bookRequest.title.isBlank()) "empty" else "invalid",
                field = "title",
                message = "Field title must not be empty and must contain text"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateSeriesPair(title: String) = worker {
    this.title = title
    this.description = "Проверка что поля series и seriesNumber одновременно заполнены или одновременно пусты"
    on {
        state == StateModel.RUNNING &&
            (bookRequest.series.isNullOrBlank() && bookRequest.seriesNumber != null) ||
            (!bookRequest.series.isNullOrBlank() && bookRequest.seriesNumber == null)
    }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "series/seriesNumber",
                message = "Fields series and seriesNumber must be both filled or both empty"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateSeries(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле series пустое или содержит что-то осмысленное"
    on { state == StateModel.RUNNING && !bookRequest.series.isNullOrBlank() && !bookRequest.series!!.validateContent() }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "series",
                message = "Field series must be empty or contain text"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateSeriesNumber(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле seriesNumber содержит положительное целое число"
    on { state == StateModel.RUNNING && bookRequest.seriesNumber != null && bookRequest.seriesNumber!! <= 0 }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "seriesNumber",
                message = "Field seriesNumber must be a positive integer"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateYear(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле year содержит валидный год из диапазона от 0 до текущего года"
    val currentYear = java.time.LocalDate.now().year
    on { state == StateModel.RUNNING && (bookRequest.year != null && (bookRequest.year!! !in 0..currentYear)) }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "year",
                message = "Field year must be between 0 and $currentYear"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateUrl(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле url пустое или содержит валидный URL-адрес"
    val urlRegex = Pattern.compile(
        "^(https?://)?" +
            "([\\p{IsCyrillic}\\p{IsLatin}\\d\\-.]+\\.)+" +
            "([a-z]{2,63}|\\p{IsCyrillic}+)" +
            "(/?[\\p{IsCyrillic}\\p{IsLatin}\\d\\-._~!$&'()*+,;=:@%]*)*" +
            "(\\#.*)?$", Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CHARACTER_CLASS
    )
    on { state == StateModel.RUNNING && !bookRequest.url.isNullOrBlank() && !urlRegex.matcher(bookRequest.url).matches() }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "url",
                message = "Field url must be empty or a valid URL address"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateDateStart(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле dateStart пустое или содержит валидную дату в формате YYYY-MM-DD"
    val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}\$")
    on { state == StateModel.RUNNING && !bookRequest.dateStart.isNullOrBlank() && !bookRequest.dateStart!!.matches(dateRegex) }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "dateStart",
                message = "Field dateStart must be empty or a valid date in format YYYY-MM-DD"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateDateEnd(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле dateEnd пустое или содержит валидную дату в формате YYYY-MM-DD"
    val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}\$")
    on { state == StateModel.RUNNING && !bookRequest.dateEnd.isNullOrBlank() && !bookRequest.dateEnd!!.matches(dateRegex) }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "dateEnd",
                message = "Field dateEnd must be empty or a valid date in format YYYY-MM-DD"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateDates(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле dateEnd больше или равно полю dateStart"
    on {
        state == StateModel.RUNNING &&
            !bookRequest.dateStart.isNullOrBlank() &&
            !bookRequest.dateEnd.isNullOrBlank() &&
            bookRequest.dateEnd!! < bookRequest.dateStart!!
    }
    handle {
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "dateStart/dateEnd",
                message = "Field dateEnd must be greater than or equal to dateStart"
            )
        )
    }
}

fun ICorChainDsl<Context>.validateSearchString(title: String) = worker {
    this.title = title
    this.description = "Проверка что поле searchString не пустое и содержит что-то осмысленное"
    on { state == StateModel.RUNNING && !bookFilterRequest.searchString.isBlank() && !bookFilterRequest.searchString.validateContent() }
    handle {
        state = StateModel.FAILING
        errors.add(
            ErrorModel(
                group = "validation",
                code = "invalid",
                field = "searchString",
                message = "Field searchString must not be empty and must contain text"
            )
        )
    }
}

fun String.validateContent(): Boolean {
    val regExpL = Regex("\\p{L}")
    val regExpN = Regex("\\d")
    return this.contains(regExpL) || this.contains(regExpN)
}