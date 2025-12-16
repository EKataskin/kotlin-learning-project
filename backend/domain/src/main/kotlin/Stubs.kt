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
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubCreateSuccess…" }
        state = StateModel.FINISHING
        bookResponse = Stubs.BOOK1
    }
}

fun ICorChainDsl<Context>.stubBadAuthor(title: String) = worker {
    this.title = title
    this.description = "Ошибка данных в атрибуте author"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.BAD_AUTHOR && state == StateModel.RUNNING }
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
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.BAD_TITLE && state == StateModel.RUNNING }
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

fun ICorChainDsl<Context>.stubBadUrl(title: String) = worker {
    this.title = title
    this.description = "Ошибка данных в атрибуте url"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.BAD_URL && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubBadUrl…" }
        errors.add(ErrorModel(
            group = "validation",
            code = "validation-url",
            field = "url",
            message = "Wrong url field"
        ))
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.stubBadId(title: String) = worker {
    this.title = title
    this.description = "Ошибка данных в атрибуте id"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.BAD_ID && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubBadId…" }
        errors.add(ErrorModel(
            group = "validation",
            code = "validation-id",
            field = "id",
            message = "Wrong id field"
        ))
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.stubUnknownCase(title: String) = worker {
    this.title = title
    this.description = "Имитируем ситуацию, когда запрошен неизвестный кейс"
    on { environment == EnvironmentModel.STUB && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubUnknownCase…" }
        errors.add(ErrorModel(
            group = "validation",
            code = "unknown stub",
            message = "Unknown stub case is requested: ${stubCase.name}"
        ))
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.stubReadSuccess(title: String) = worker {
    this.title = title
    this.description = "Успешное чтение книги"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubReadSuccess…" }
        state = StateModel.FINISHING
        bookResponse = Stubs.BOOK1
    }
}

fun ICorChainDsl<Context>.stubNotFound(title: String) = worker {
    this.title = title
    this.description = "Имитируем ситуацию отсутствия запрошенной книги"
        on { environment == EnvironmentModel.STUB && stubCase == StubsModel.NOT_FOUND && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubNotFound…" }
        errors.add(ErrorModel(
            group = "search",
            code = "not found",
            message = "Requested object not found: ${id.asString()}"
        ))
        state = StateModel.FAILING
    }
}

fun ICorChainDsl<Context>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    this.description = "Успешное обновление книги"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubUpdateSuccess…" }
        state = StateModel.FINISHING
        bookResponse = Stubs.BOOK2
    }
}

fun ICorChainDsl<Context>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    this.description = "Успешное удаление книги"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubDeleteSuccess…" }
        state = StateModel.FINISHING
        bookResponse = BookModel.NONE
    }
}

fun ICorChainDsl<Context>.stubSearchSuccess(title: String) = worker {
    this.title = title
    this.description = "Успешный поиск книг"
    on { environment == EnvironmentModel.STUB && stubCase == StubsModel.SUCCESS && state == StateModel.RUNNING }
    handle {
        log.debug { "Started stubSearchSuccess…" }
        state = StateModel.FINISHING
        booksResponse.addAll(
            listOf(
                Stubs.BOOK1,
                Stubs.BOOK2,
            )
        )
    }
}