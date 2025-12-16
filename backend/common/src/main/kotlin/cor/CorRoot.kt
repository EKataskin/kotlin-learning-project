package ru.ekataskin.booktracker.common.cor

/**
 * Точка входа в dsl построения цепочек.
 * Элементы исполняются последовательно.
 *
 * Пример:
 * ```
 *  rootChain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *  }
 * ```
 */
fun <T> rootChain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> = CorChainDsl<T>().apply(function)
