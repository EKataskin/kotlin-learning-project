package ru.ekataskin.booktracker.common.interfaces

/**
 * Блок кода, который обрабатывает контекст
 */
interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}