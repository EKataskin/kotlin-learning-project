package ru.ekataskin.booktracker.domain

import io.github.oshai.kotlinlogging.KotlinLogging
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.cor.*
import ru.ekataskin.booktracker.common.models.*
import ru.ekataskin.booktracker.stubs.Stubs

private val log = KotlinLogging.logger {}

fun ICorChainDsl<Context>.stubCreateSuccess(title: String) = worker {
    this.title = title
    this.description = "Успешное создание книги"
    on { stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubCreateSuccess…" }
        state = StateModel.FINISHING
        bookResponse = Stubs.BOOK1
    }
}

fun ICorChainDsl<Context>.stubBadAuthor(title: String) = worker {
    this.title = title
    this.description = "Ошибка данных в атрибуте author"
    on { stubCase == StubsModel.BAD_AUTHOR && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubBadAuthor…" }
        errors.add(ErrorModel(
            group = "validation",
            code = "validation-author",
            field = "author",
            message = "Wrong author field"
        ))
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.stubBadTitle(title: String) = worker {
    this.title = title
    this.description = "Ошибка данных в атрибуте title"
    on { stubCase == StubsModel.BAD_TITLE && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubBadTitle…" }
        errors.add(ErrorModel(
            group = "validation",
            code = "validation-title",
            field = "title",
            message = "Wrong title field"
        ))
        state = StateModel.FAILING
    }
}
