package com.brieuclf.library.domain.model

import java.util.UUID
import kotlinx.datetime.Instant

data class Borrowing(
    val bookId: UUID,
    val instant: Instant,
    val id: UUID
)

data class BorrowBookInformation(
    val bookId: UUID,
    val instant: Instant
)