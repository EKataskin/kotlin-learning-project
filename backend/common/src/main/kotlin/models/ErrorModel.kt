package ru.ekataskin.booktracker.common.models

data class ErrorModel (
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
)