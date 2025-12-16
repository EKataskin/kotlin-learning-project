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