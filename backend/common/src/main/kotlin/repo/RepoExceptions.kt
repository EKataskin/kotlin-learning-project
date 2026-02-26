package ru.ekataskin.booktracker.common.repo

import ru.ekataskin.booktracker.common.models.BookIdModel
import ru.ekataskin.booktracker.common.models.LockModel

open class RepoException(
    val id: BookIdModel,
    msg: String,
): Exception(msg)

class RepoEmptyLockException(id: BookIdModel) : RepoException(
    id,
    "Lock is empty in DB"
)

class RepoConcurrencyException(id: BookIdModel, expectedLock: LockModel, actualLock: LockModel?) : RepoException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)