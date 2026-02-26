package ru.ekataskin.booktracker.common

import ru.ekataskin.booktracker.common.models.ErrorModel

fun systemError(
    code: String,
    message: String? = null,
    e: Throwable? = null,
): ErrorModel {
    val fullMessage = buildString {
        if (message != null) {
            append(message)
        }
        if (e != null) {
            if (isNotEmpty()) append(" | ")
            append("Exception: ${e.message}")
        }
    }
    return ErrorModel(
        code = "system-$code",
        group = "system",
        message = fullMessage.ifEmpty { "System error occurred" },
        exception = e
    )
}