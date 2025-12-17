package ru.ekataskin.booktracker.domain

import io.github.oshai.kotlinlogging.withLoggingContext
import ru.ekataskin.booktracker.common.Context
import ru.ekataskin.booktracker.common.cor.*
import ru.ekataskin.booktracker.common.interfaces.IDomain
import ru.ekataskin.booktracker.common.models.*

class Domain : IDomain {
    override suspend fun exec(ctx: Context) {
        withLoggingContext("requestId" to ctx.requestId.asString()) {
            domainChain.exec(ctx)
        }
    }

    private val domainChain = rootChain<Context> {
        worker {
            title = "Старт обработки"
            description = "Воркер для инициализации обработки (присваивает статус RUNNING)"
            on { state == StateModel.NONE }
            handle {state = StateModel.RUNNING }
        }

        operation("Создание книги", CommandModel.CREATE) {
            stubCreateSuccess("Имитация успешной обработки")
            stubBadAuthor("Имитация ошибки в поле author")
            stubBadTitle("Имитация ошибки в поле title")
            stubBadUrl("Имитация ошибки в поле url")
            stubUnknownCase("Ошибка: неизвестный кейс")

            validateAuthor("Валидация поля author")
            validateTitle("Валидация поля title")
            validateSeriesPair("Валидация пары series + seriesNumber")
            validateSeries("Валидация поля series")
            validateSeriesNumber("Валидация поля seriesNumber")
            validateYear("Валидация поля year")
            validateUrl("Валидация поля url")
            validateDateStart("Валидация поля dateStart")
            validateDateEnd("Валидация поля dateEnd")
            validateDates("Валидация порядка dateStart и dateEnd")
            collectValidationErrors("Сбор ошибок валидации")
        }

        operation("Получение книги", CommandModel.READ) {
            stubReadSuccess("Имитация успешной обработки")
            stubBadId("Имитация ошибки в поле id")
            stubNotFound("Имитация ошибки: книга не найдена")
            stubUnknownCase("Ошибка: неизвестный кейс")

            validateId("Валидация поля id")
            collectValidationErrors("Сбор ошибок валидации")
        }

        operation("Изменение книги", CommandModel.UPDATE) {
            stubUpdateSuccess("Имитация успешной обработки")
            stubBadId("Имитация ошибки в поле id")
            stubBadAuthor("Имитация ошибки в поле author")
            stubBadTitle("Имитация ошибки в поле title")
            stubBadUrl("Имитация ошибки в поле url")
            stubNotFound("Имитация ошибки: книга не найдена")
            stubUnknownCase("Ошибка: неизвестный кейс")

            validateId("Валидация поля id")
            validateAuthor("Валидация поля author")
            validateTitle("Валидация поля title")
            validateSeriesPair("Валидация пары series + seriesNumber")
            validateSeries("Валидация поля series")
            validateSeriesNumber("Валидация поля seriesNumber")
            validateYear("Валидация поля year")
            validateUrl("Валидация поля url")
            validateDateStart("Валидация поля dateStart")
            validateDateEnd("Валидация поля dateEnd")
            validateDates("Валидация порядка dateStart и dateEnd")
            collectValidationErrors("Сбор ошибок валидации")
        }

        operation("Удаление книги", CommandModel.DELETE) {
            stubDeleteSuccess("Имитация успешной обработки")
            stubBadId("Имитация ошибки в поле id")
            stubNotFound("Имитация ошибки: книга не найдена")
            stubUnknownCase("Ошибка: неизвестный кейс")

            validateId("Валидация поля id")
            collectValidationErrors("Сбор ошибок валидации")
        }

        operation("Поиск книг", CommandModel.SEARCH) {
            stubSearchSuccess("Имитация успешной обработки")
            stubBadAuthor("Имитация ошибки в поле author")
            stubBadTitle("Имитация ошибки в поле title")
            stubUnknownCase("Ошибка: неизвестный кейс")

            validateSearchString("Валидация поисковой строки")
        }

    }.build()
}

fun ICorChainDsl<Context>.operation(
    title: String,
    command: CommandModel,
    block: ICorChainDsl<Context>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == StateModel.RUNNING }
}